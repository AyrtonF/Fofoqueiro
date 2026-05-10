package com.security.fofoqueiro.domain.ports;

public interface IS3StoragePort {
    String uploadFile(String bucketName, String key, byte[] fileContent, String contentType);
    byte[] downloadFile(String bucketName, String key);
    void deleteFile(String bucketName, String key);
    String generatePresignedUrl(String bucketName, String key, long expirationMinutes);
}
