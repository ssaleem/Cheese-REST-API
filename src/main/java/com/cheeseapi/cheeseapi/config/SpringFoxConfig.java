package com.cheeseapi.cheeseapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
//@PropertySource("classpath:swagger.properties")
public class SpringFoxConfig {

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cheeseapi"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Cheeses REST Api",
                "A Spring Boot REST API to perform CRUD operations on Cheeses, Cheese Categories and Menus",
                "1.0.0",
                "TERMS OF SERVICE URL",
                new Contact("Sara Saleem", "http://www.sarasaleem.com", "sara@sarasaleem.com"),
                "MIT License",
                "https://opensource.org/licenses/MIT",
                Collections.emptyList()
        );
    }

}