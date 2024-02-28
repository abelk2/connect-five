package eu.abelk.connectfive.server.config;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfig {

    @Bean
    public OperationCustomizer globalParametersOperationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operation.setTags(operation.getTags()
                .stream()
                .map(tag -> tag.replace("-rest-controller", "-backend"))
                .toList());
            return operation;
        };
    }

}
