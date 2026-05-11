package com.security.fofoqueiro.infrastructure.security;

import com.security.fofoqueiro.domain.dtos.AuthResponseDTO;
import com.security.fofoqueiro.domain.dtos.UserAuthDTO;
import com.security.fofoqueiro.domain.exceptions.AuthException;
import com.security.fofoqueiro.domain.models.Tenant;
import com.security.fofoqueiro.domain.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthTokenService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HEADER_JSON = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final long ACCESS_TTL_SECONDS = 60L * 60L;
    private static final long REFRESH_TTL_SECONDS = 60L * 60L * 24L * 7L;
    private static final long MFA_TTL_SECONDS = 60L * 5L;

    private final String secret;
    private final Map<String, MfaChallenge> mfaChallenges = new ConcurrentHashMap<>();

    public AuthTokenService(@Value("${app.jwt.secret:fofoqueiro-dev-secret-change-me}") String secret) {
        this.secret = secret;
    }

    public AuthResponseDTO createAuthResponse(User user, Tenant tenant, UserAuthDTO authUser, boolean mfaRequired, String mfaToken) {
        String accessToken = createJwt(user, tenant, "access", ACCESS_TTL_SECONDS);
        String refreshToken = createJwt(user, tenant, "refresh", REFRESH_TTL_SECONDS);

        return AuthResponseDTO.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .mfaRequired(mfaRequired)
                .mfaToken(mfaToken)
                .user(authUser)
                .tenantId(tenant != null && tenant.getId() != null ? String.valueOf(tenant.getId()) : null)
                .build();
    }

    public String createMfaChallengeToken(User user, Tenant tenant) {
        String challengeId = UUID.randomUUID().toString();
        String code = generateDevMfaCode(user.getId());
        long expiresAt = Instant.now().getEpochSecond() + MFA_TTL_SECONDS;
        mfaChallenges.put(challengeId, new MfaChallenge(challengeId, user.getId(), code, expiresAt));
        return createToken("mfa", challengeId, user, tenant, MFA_TTL_SECONDS);
    }

    public MfaChallenge validateMfaChallenge(String mfaToken, String code) {
        JwtPayload payload = parseAndValidate(mfaToken, "mfa");
        MfaChallenge challenge = mfaChallenges.get(payload.challengeId());
        if (challenge == null) {
            throw new AuthException("MFA challenge expired");
        }
        if (challenge.expiresAt() < Instant.now().getEpochSecond()) {
            mfaChallenges.remove(challenge.challengeId());
            throw new AuthException("MFA challenge expired");
        }
        if (!challenge.code().equals(code)) {
            throw new AuthException("Invalid MFA code");
        }
        mfaChallenges.remove(challenge.challengeId());
        return challenge;
    }

    public JwtPayload validateAccessToken(String token) {
        return parseAndValidate(token, "access");
    }

    public JwtPayload validateRefreshToken(String token) {
        return parseAndValidate(token, "refresh");
    }

    public AuthResponseDTO refresh(String refreshToken) {
        JwtPayload payload = validateRefreshToken(refreshToken);
        String name = payload.name() != null ? payload.name() : payload.sub();
        String firstName = name;
        String lastName = "";
        if (name != null && name.contains(" ")) {
            firstName = name.substring(0, name.indexOf(' '));
            lastName = name.substring(name.indexOf(' ') + 1);
        }
        User user = User.builder()
            .id(payload.userId())
            .email(payload.sub())
            .firstName(firstName)
            .lastName(lastName)
            .tenantId(payload.tenantId())
            .build();
        Tenant tenant = Tenant.builder().id(payload.tenantId()).build();
        UserAuthDTO authUser = UserAuthDTO.builder()
                .id(payload.userId())
                .email(payload.sub())
            .name(name)
                .role("ADMIN")
                .build();
        return createAuthResponse(user, tenant, authUser, false, null);
    }

    private String createJwt(User user, Tenant tenant, String type, long ttlSeconds) {
        long exp = Instant.now().getEpochSecond() + ttlSeconds;
        String payload = "{"
                + "\"sub\":\"" + escape(user.getEmail()) + "\"," 
                + "\"userId\":" + user.getId() + ","
                + "\"tenantId\":" + tenant.getId() + ","
                + "\"name\":\"" + escape((user.getFirstName() + " " + user.getLastName()).trim()) + "\"," 
                + "\"type\":\"" + type + "\"," 
                + "\"exp\":" + exp
                + "}";
        return sign(payload);
    }

    private String createToken(String type, String challengeId, User user, Tenant tenant, long ttlSeconds) {
        long exp = Instant.now().getEpochSecond() + ttlSeconds;
        String payload = "{"
                + "\"sub\":\"" + escape(user.getEmail()) + "\"," 
                + "\"userId\":" + user.getId() + ","
                + "\"tenantId\":" + tenant.getId() + ","
                + "\"challengeId\":\"" + challengeId + "\"," 
                + "\"type\":\"" + type + "\"," 
                + "\"exp\":" + exp
                + "}";
        return sign(payload);
    }

    private String sign(String payloadJson) {
        String encodedHeader = base64Url(HEADER_JSON.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));
        String content = encodedHeader + "." + encodedPayload;
        String signature = base64Url(hmac(content.getBytes(StandardCharsets.UTF_8)));
        return content + "." + signature;
    }

    private JwtPayload parseAndValidate(String token, String expectedType) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new AuthException("Invalid token format");
            }
            String content = parts[0] + "." + parts[1];
            String expectedSignature = base64Url(hmac(content.getBytes(StandardCharsets.UTF_8)));
            if (!MessageDigest.isEqual(expectedSignature.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
                throw new AuthException("Invalid token signature");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            JwtPayload payload = JwtPayload.fromJson(payloadJson);
            if (!expectedType.equals(payload.type())) {
                throw new AuthException("Invalid token type");
            }
            if (payload.exp() < Instant.now().getEpochSecond()) {
                throw new AuthException("Token expired");
            }
            return payload;
        } catch (IllegalArgumentException ex) {
            throw new AuthException("Invalid token");
        }
    }

    private byte[] hmac(byte[] content) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return mac.doFinal(content);
        } catch (Exception ex) {
            throw new AuthException("Unable to sign token");
        }
    }

    private String base64Url(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String generateDevMfaCode(Long userId) {
        long normalized = Math.abs(userId == null ? 123456L : userId * 9973L);
        return String.format("%06d", normalized % 1_000_000L);
    }

    public record MfaChallenge(String challengeId, Long userId, String code, long expiresAt) {}

    public record JwtPayload(String sub, Long userId, Long tenantId, String name, String type, long exp, String challengeId) {
        static JwtPayload fromJson(String json) {
            return new JwtPayload(
                    extractString(json, "sub"),
                    extractLong(json, "userId"),
                    extractLong(json, "tenantId"),
                    extractString(json, "name"),
                    extractString(json, "type"),
                    extractLong(json, "exp"),
                    extractString(json, "challengeId")
            );
        }

        private static String extractString(String json, String key) {
            String marker = "\"" + key + "\":";
            int start = json.indexOf(marker);
            if (start < 0) {
                return null;
            }
            start += marker.length();
            if (start >= json.length() || json.charAt(start) == 'n') {
                return null;
            }
            if (json.charAt(start) != '"') {
                return null;
            }
            int firstQuote = json.indexOf('"', start);
            int secondQuote = json.indexOf('"', firstQuote + 1);
            return json.substring(firstQuote + 1, secondQuote);
        }

        private static Long extractLong(String json, String key) {
            String marker = "\"" + key + "\":";
            int start = json.indexOf(marker);
            if (start < 0) {
                return null;
            }
            start += marker.length();
            int end = start;
            while (end < json.length() && Character.isDigit(json.charAt(end))) {
                end++;
            }
            return Long.valueOf(json.substring(start, end));
        }
    }
}
