package com.example.demo.web.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackageClasses = WebGraphQlHandler.class)
public class GraphQLResponseAdvice implements ResponseBodyAdvice<WebGraphQlResponse> {

    private static final Logger log = LoggerFactory.getLogger(GraphQLResponseAdvice.class);
                     

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        boolean isSupported = WebGraphQlResponse.class.isAssignableFrom(returnType.getParameterType());
        log.debug("supports() called. ReturnType: {}, ConverterType: {}, isSupported: {}", 
                     returnType.getParameterType().getSimpleName(), converterType.getSimpleName(), isSupported);
        return isSupported;
    }

    @Override
    @Nullable
    public WebGraphQlResponse beforeBodyWrite(@Nullable WebGraphQlResponse body, 
                                              @NonNull MethodParameter returnType, 
                                              @NonNull MediaType selectedContentType, 
                                              @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, 
                                              @NonNull ServerHttpRequest request, 
                                              @NonNull ServerHttpResponse response) {

        log.info("GraphQL Response: {}", body);                                        
                                                
        if (body == null || body.getErrors().isEmpty()) {
            return body;
        }

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        for (ResponseError error : body.getErrors()) {
            if (error.getExtensions().containsKey("status")) {
                int statusCode = ((Integer) error.getExtensions().get("status")).intValue();
                status = HttpStatus.valueOf(statusCode);
                break;
            }
        }

        response.setStatusCode(status);
        return body;
    }
}

