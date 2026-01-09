package com.ticktickdoc.storage.service;

import com.ticktickdoc.storage.domain.FileDomain;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioFileStorageServiceImpl implements FileStorageService {

    @Value("${minio.bucket-name}")
    private String bucketName;

    private final MinioClient minioClient;

    @PostConstruct
    private void initBucket() {
        try {
            BucketExistsArgs bucketExistsArgs = new BucketExistsArgs.Builder()
                    .bucket(bucketName)
                    .build();
            if (!minioClient.bucketExists(bucketExistsArgs)) {
                minioClient.makeBucket(new MakeBucketArgs.Builder()
                        .bucket(bucketName)
                        .build());
                log.info("Bucket MinIO create");
            }
        } catch (Exception ex) {
            throw new RuntimeException("MinIO error initialization", ex);
        }
    }

    @Override
    public FileDomain uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String uuidFile = String.valueOf(UUID.randomUUID());
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uuidFile)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return new FileDomain(null ,uuidFile, fileName, LocalDateTime.now());
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error file-storage", e);
        }
    }

    @Override
    public Resource downloadFile(String filename) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
            return new InputStreamResource(stream);
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error file-storage", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error file-storage", e);
        }
    }
}
