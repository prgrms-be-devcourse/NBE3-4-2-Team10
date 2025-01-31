package com.ll.TeamProject.global.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API 서버", version = "v1"))
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi scheduleApi() {
        return GroupedOpenApi.builder()
                .group("Schedule")
                .pathsToMatch("/schedules/**")
                .build();
    }
}
