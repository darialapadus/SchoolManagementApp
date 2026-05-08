package com.example.school_managementv1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI schoolManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("School Management API")
                        .version("v1")
                        .description("REST API for managing students, teachers, classrooms, courses, enrollments, grades and attendance."));
    }
}
