package com.televisory.capitalmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com"))
                //.apis(RequestHandlerSelectors.any())
                //.paths(PathSelectors.any())
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(getApiInfo());
    }
	
	private ApiInfo getApiInfo() {
		
		Contact contact = new Contact("Televisory","https://www.televisory.com","sd@televisory.com");
        return new ApiInfoBuilder()
                .title("Televisory Capital Market Rest Service")
                .description("Back-end Rest service for Televisory Capital Market")
                .version("0.1")
                .license("Copyright © 2018 Televisory | All Rights Reserved")
                .licenseUrl("https://www.televisory.com/policy")
                .contact(contact)
                .build();
	
	}
}
