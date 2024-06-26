package com.skipp.enlistment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.ApiInfo;

@Configuration
public class SwaggerConfig {
    @Bean

    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.skipp.enlistment.web"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SKIPP Capstone")
                .description("Swagger API documentation for the SKIPP Capstone Project :)")
                .version("1.0")
                .build();
    }
}
