package com.example.demo.web.configuration;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
public class CustomInstrumentation extends SimpleInstrumentation {

    private static final Logger logger = LoggerFactory.getLogger(CustomInstrumentation.class);
    private final GraphQLResponseAdvice responseAdvice;

    public CustomInstrumentation(GraphQLResponseAdvice responseAdvice) {
        this.responseAdvice = responseAdvice;
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        return SimpleInstrumentation.super.beginExecution(parameters);
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        logger.debug("Instrumenting execution result");

        return CompletableFuture.completedFuture(executionResult).thenApply(result -> {
            WebGraphQlResponse response = WebGraphQlResponse.create(result);
            
            if (!response.getErrors().isEmpty()) {
                HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                for (org.springframework.graphql.execution.ErrorType error : response.getErrors()) {
                    if (error.getExtensions().containsKey("status")) {
                        int statusCode = ((Integer) error.getExtensions().get("status")).intValue();
                        status = HttpStatus.valueOf(statusCode);
                        logger.info("Setting HTTP status to {} based on GraphQL error", status);
                        break;
                    }
                }
                
                // Here we're simulating the behavior of ResponseBodyAdvice
                // In a real scenario, you'd need to find a way to set the HTTP status on the actual response
                logger.debug("GraphQL response processed. Status: {}, Errors: {}", status, response.getErrors());
            } else {
                logger.debug("No errors in GraphQL response");
            }

            return result;
        });
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        return (DataFetcher<Object>) environment -> {
            Object result = dataFetcher.get(environment);
            if (result instanceof CompletableFuture) {
                return ((CompletableFuture<?>) result).handle((data, throwable) -> {
                    if (throwable != null) {
                        logger.error("Error in data fetcher", throwable);
                        // Here you could add custom error handling if needed
                    }
                    return data;
                });
            }
            return result;
        };
    }
}