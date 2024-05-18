package com.hcl.ownermicroservice.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
@Configuration   //project settings
@EnableSwagger2  //activate swagger file in the background
public class SwaggerController
{
        @Bean  //from the start of project running to the end
        public Docket libraryApi()   //pluggable
        {  
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .groupName("ShareRide-Api")
                    .select().apis(RequestHandlerSelectors.basePackage("com.hcl.ownermicroservice.controller"))  
                    .build()
    				.securitySchemes(Arrays.asList(apiKey()))
                    .securityContexts(Arrays.asList(securityContext()))
                    .apiInfo(apiInfo())
                    .pathMapping("/")
                    .useDefaultResponseMessages(false)
                    .directModelSubstitute(LocalDate.class, String.class)
                    .genericModelSubstitutes(ResponseEntity.class);

        }
      
        private ApiInfo apiInfo()
        {  // used this object to store the meta data
            return new ApiInfoBuilder().title("Share Ride Api")
                    .description("Share Ride Owner Swagger")
                    .build();
        }
       private ApiKey apiKey() {
            return new ApiKey("JWT", "Authorization", "header");
        }

        private SecurityContext securityContext() {
            return SecurityContext.builder().securityReferences(defaultAuth()).build();
        }
        private List<SecurityReference> defaultAuth() {
            AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
            authorizationScopes[0] = authorizationScope;
            return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
        }
        @Bean
        public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
                List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
                Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
                allEndpoints.addAll(webEndpoints);
                allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
                allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
                String basePath = webEndpointProperties.getBasePath();
                EndpointMapping endpointMapping = new EndpointMapping(basePath);
                boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
                return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
            }


        private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
                return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
            }
}
