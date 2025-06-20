package tech.bgdigital.online.payment.config.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//doc https://www.baeldung.com/spring-boot-swagger-jwt
//doc https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
//doc https://www.javainuse.com/spring/boot_swagger_annotations
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        List<SecurityScheme> apiKeys = new ArrayList<SecurityScheme>();
          apiKeys.add(apiKey());
          apiKeys.add(secreteKey());
          apiKeys.add(bearerJwt());
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .securitySchemes(apiKeys)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Test technique",
                "Test technique by CheikhFall KHOUMA",
                "API 1.0",
                "Terms of service",
                new Contact("PSN", "", "cfkhouma@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }

    private ApiKey bearerJwt() {
        return new ApiKey("JWT", "Authorization", "header");
    }
    private ApiKey apiKey() {
        return new ApiKey("Application Key", "app-key", "header");
    }
    private ApiKey secreteKey() {
        return new ApiKey("Secrete Key", "secrete-key", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<SecurityReference>();
        securityReferences.add(new SecurityReference("JWT", authorizationScopes));
        securityReferences.add(new SecurityReference("Application Key", authorizationScopes));
        securityReferences.add(new SecurityReference("Secrete Key", authorizationScopes));
        return securityReferences;
    }

}
