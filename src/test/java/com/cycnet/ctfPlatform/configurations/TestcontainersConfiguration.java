package com.cycnet.ctfPlatform.configurations;

import io.minio.MinioClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;


@TestConfiguration
public class TestcontainersConfiguration {

    private static final String POSTGRE_SQL_IMAGE_NAME = "postgres:16-alpine";
    private static final String MINIO_IMAGE_NAME = "minio/minio:latest";

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>(POSTGRE_SQL_IMAGE_NAME);
    }

    @Bean
    public MinIOContainer minIOContainer() {
        return new MinIOContainer(MINIO_IMAGE_NAME);
    }

    @Bean
    public MinioClient minioClient(MinIOContainer minIOContainer) {
        return MinioClient.builder()
                .endpoint(minIOContainer.getS3URL())
                .credentials(
                        minIOContainer.getUserName(),
                        minIOContainer.getPassword()
                )
                .build();
    }

}
