package com.example.demo.exceptions;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.lang.NonNull;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomGraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        if (ex instanceof GraphQLCustomException) {
            GraphQLCustomException customException = (GraphQLCustomException) ex;
            Map<String, Object> extensions = new HashMap<>();
            extensions.put("status", customException.getStatus().value());
            
            return GraphqlErrorBuilder.newError()
                    .message("this is cutomize message" + customException.getMessage())
                    .extensions(extensions)
                    .build();
        }
        return super.resolveToSingleError(ex, env);
    }
}
