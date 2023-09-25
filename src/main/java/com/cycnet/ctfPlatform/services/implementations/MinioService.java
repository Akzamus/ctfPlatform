package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.configurations.properties.MinioProperties;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.exceptions.server.InternalServerErrorException;
import com.cycnet.ctfPlatform.services.StorageService;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService implements StorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String uploadFile(String filePath, InputStream inputStream) {
        try {
            log.info("Uploading file with file name: {}", filePath);

            ObjectWriteResponse objectWriteResponse = minioClient.putObject(
                    PutObjectArgs.builder()
                            .stream(inputStream, inputStream.available(), -1)
                            .bucket(minioProperties.bucketName())
                            .object(filePath)
                            .build()
            );

            log.info("Uploaded file: {}", filePath);

            return objectWriteResponse.object();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getFile(String filePath) {
        try {
            throwExceptionIfFileDoesNotExists(filePath);

            log.info("Downloading file with file name: {}", filePath);

            GetObjectResponse getObjectResponse = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.bucketName())
                            .object(filePath)
                            .build()
            );

            log.info("Downloaded file: {}", filePath);

            return getObjectResponse.readAllBytes();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to download file: " + e.getMessage(), e);
        }
    }

    @Override
    public String renameFile(String oldPath, String newPath) {
        try {
            throwExceptionIfFileDoesNotExists(oldPath);

            log.info("Renaming file from {} to {}", oldPath, newPath);

            ObjectWriteResponse objectWriteResponse = minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(minioProperties.bucketName())
                            .object(oldPath)
                            .source(
                                    CopySource.builder()
                                            .bucket(minioProperties.bucketName())
                                            .object(newPath)
                                            .build()
                            )
                            .build()
            );

            deleteFile(oldPath);

            log.info("File renamed from {} to {}", oldPath, newPath);

            return objectWriteResponse.object();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to rename file: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            throwExceptionIfFileDoesNotExists(path);

            log.info("Deleting file with file name: {}", path);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.bucketName())
                            .object(path)
                            .build()
            );

            log.info("Deleted file: {}", path);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    private void throwExceptionIfFileDoesNotExists(String fileName) throws Exception {
        StatObjectResponse statObjectResponse = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(minioProperties.bucketName())
                        .object(fileName)
                        .build()
        );

        if (statObjectResponse == null) {
            throw new EntityNotFoundException("File with name " + fileName + " does not exist");
        }
    }

    @PostConstruct
    private void createBucketIfDoesNotExists() throws Exception {
        String bucketName = minioProperties.bucketName();

        boolean isBucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        if (!isBucketExists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            log.warn("Created new backed with name: {}", bucketName);
        }
    }

}
