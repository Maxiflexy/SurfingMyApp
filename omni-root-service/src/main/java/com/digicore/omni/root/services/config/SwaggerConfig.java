package com.digicore.omni.root.services.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Apr-23(Sun)-2023
 */




@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    //todo externalize configs where applicable

    @Value("${omni.swagger.dev.url:https://uat.digicoreltd.com/zest-root}")
    private String swaggerUrl;

    @Bean
    public OpenAPI springShopOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("Payment Gateway Merchant/BackOffice APIs")
                        .description("This documentation contains all the APIs exposed for the merchant and backoffice console")
                        .version("v1.0.0")
                        .license(new License().name("Proprietary License").url("http://localhost:2760/download-license")))
                .externalDocs(new ExternalDocumentation()
                        .description("Issue tracker")
                        .url("https://docs.google.com/spreadsheets/d/1xxqqKQg4cjw1UnNAIZHxxxb7tqpdetE_wCvrcm5rWVU/edit#gid=1515685221"))

                .addServersItem(new Server().description("Local Server").url("http://localhost:2759"))
                .addServersItem(new Server().description("Redpay Development Server").url(swaggerUrl))
//                .addServersItem(new Server().description("Digicore Development Server").url(swaggerUrl))
              //  .addServersItem(new Server().description("Zest Development Server").url("https://consumer.dev.gateway.zestpayment.com"))
//                .addServersItem(new Server().description("Octopus Development Server").url("https://api-dev.withoctopus.com/root"))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                );
    }

    @Bean
    public GroupedOpenApi merchantApi() {
        return GroupedOpenApi.builder()
                .group("merchant")
                .pathsToMatch("/api/v1/merchant-**/**")
                .build();
    }

    @Bean
    public GroupedOpenApi backofficeApi() {
        return GroupedOpenApi.builder()
                .group("backoffice")
                .pathsToMatch("/api/v1/backoffice-**/**")
                .build();
    }



}
