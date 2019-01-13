package com.cenkc.jpademo.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;

/**
 * created by cenkc on 1/13/2019
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final Contact DEFAULT_CONTACT = new Contact("Cenk Canarslan","https://github.com/cenkc","cenk.canarslan@gmail.com");

    public Docket produceAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(getApiInfo())
                    .select()
                    .apis(getApis())
                    .paths(getPaths())
                    .build()
                    .directModelSubstitute(LocalDate.class, String.class)
                    .genericModelSubstitutes(ResponseEntity.class)
                ;
    }

    private Predicate<RequestHandler> getApis() {

        return RequestHandlerSelectors.basePackage("com.cenkc.jpademo.controller");

        // Or any apis
        // return RequestHandlerSelectors.any();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("JPA Demo Application")
                .description("This page lists all REST endpoints")
                .version("1.0-SNAPSHOT")
                .contact(DEFAULT_CONTACT)
                .build();
    }

    private Predicate<String> getPaths() {
        // Match all paths except /error
        return Predicates.and(
                PathSelectors.regex("/accounts.*"),
                Predicates.not(PathSelectors.regex("/error.*"))
        );

        // Or any path
        // return PathSelectors.any();
    }
}
