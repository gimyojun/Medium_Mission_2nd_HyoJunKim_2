package com.ll.medium.global.springDoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "medium REST API", version = "v1"))
public class SpringDocConfig {
    @Bean
    public GroupedOpenApi apiGroupV1() {
        return GroupedOpenApi.builder()
                .group("group1")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiGroupV2() {
        return GroupedOpenApi.builder()
                .group("group2")
                .pathsToMatch("/api/v2/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiGroupOthers() {
        return GroupedOpenApi.builder()
                .group("group3")
                .pathsToExclude("/api/**")
                .build();
    }
}
