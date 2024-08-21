package com.example.demo.web.configuration;

import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;

import com.example.demo.exceptions.CustomGraphQLExceptionResolver;

@Configuration
public class GraphQLConfig {

    @Bean
    public DataFetcherExceptionResolver exceptionResolver() {
        return new CustomGraphQLExceptionResolver();
    }

    @Bean
    public GraphQlSourceBuilderCustomizer sourceBuilderCustomizer(GraphQLResponseAdvice responseAdvice) {
        return (builder) -> builder.instrumentation(
            new CustomInstrumentation(responseAdvice)
        );
    }
}
