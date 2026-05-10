package com.security.fofoqueiro.application.common;

import com.security.fofoqueiro.domain.exceptions.AccessDeniedException;
import com.security.fofoqueiro.domain.exceptions.AuthException;
import com.security.fofoqueiro.domain.exceptions.EntityNotFoundException;
import com.security.fofoqueiro.domain.exceptions.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UseCaseHandler {

    @Transactional
    public <I, O> ResponseEntity<O> execute(IUseCase<I, O> useCase, I input) {
        try {
            O result = useCase.execute(input);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException ex) {
            log.warn("Entity not found: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Returning null body to align with convention
        } catch (AccessDeniedException ex) {
            log.warn("Access denied: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (InvalidOperationException ex) {
            log.warn("Invalid operation: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IllegalArgumentException ex) { // For DTO validation failures or other illegal arguments
            log.warn("Illegal argument: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (AuthException ex) { // For authentication failures
            log.warn("Authentication failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (Exception ex) {
            log.error("An unexpected error occurred during use case execution: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
