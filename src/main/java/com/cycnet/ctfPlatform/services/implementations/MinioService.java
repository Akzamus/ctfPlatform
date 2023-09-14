package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.configurations.properties.MinioProperties;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.services.FileService;
import com.cycnet.ctfPlatform.utils.FileUtils;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService implements FileService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final ZoneId zoneId;

    @Override
    public String uploadFile(String fileName, InputStream inputStream) throws Exception {
        fileName = FileUtils.addTimestampToFileName(fileName, zoneId);

        log.info("Uploading file with file name: {}", fileName);

        ObjectWriteResponse objectWriteResponse = minioClient.putObject(
                PutObjectArgs.builder()
                        .stream(inputStream, inputStream.available(), -1)
                        .bucket(minioProperties.bucketName())
                        .object(fileName)
                        .build()
        );

        log.info("Uploaded file: {}", fileName);

        return objectWriteResponse.object();
    }

    @Override
    public byte[] getFile(String fileName) throws Exception {
        throwExceptionIfFileDoesNotExists(fileName);

        log.info("Downloading file with file name: {}", fileName);

        GetObjectResponse getObjectResponse = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.bucketName())
                        .object(fileName)
                        .build()
        );

        log.info("Downloaded file: {}", fileName);

        return getObjectResponse.readAllBytes();
    }

    @Override
    public String renameFile(String oldFileName, String newFileName) throws Exception {
        throwExceptionIfFileDoesNotExists(oldFileName);

        newFileName = FileUtils.addTimestampToFileName(newFileName, zoneId);

        log.info("Renaming file from {} to {}", oldFileName, newFileName);

        ObjectWriteResponse objectWriteResponse = minioClient.copyObject(
                CopyObjectArgs.builder()
                        .bucket(minioProperties.bucketName())
                        .object(oldFileName)
                        .source(
                                CopySource.builder()
                                        .bucket(minioProperties.bucketName())
                                        .object(newFileName)
                                        .build()
                        )
                        .build()
        );

        deleteFile(oldFileName);

        log.info("File renamed from {} to {}", oldFileName, newFileName);

        return objectWriteResponse.object();
    }

    @Override
    public void deleteFile(String fileName) throws Exception{
        throwExceptionIfFileDoesNotExists(fileName);

        log.info("Deleting file with file name: {}", fileName);

        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(minioProperties.bucketName())
                        .object(fileName)
                        .build()
        );

        log.info("Deleted file: {}", fileName);
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