package com.cycnet.ctfPlatform.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("bearerAuth")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .servers(
                        List.of(
                                new Server()
                                        .description("local")
                                        .url("http://localhost:" + serverPort)
                        )
                )
                .info(
                        new Info()
                                .title("CTF platform")
                                .description("A platform for conducting team Olympiads on computer networks")
                                .version("1.0.0")
                );
    }

}
