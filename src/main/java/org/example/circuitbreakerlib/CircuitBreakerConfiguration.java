package org.example.circuitbreakerlib;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CircuitBreakerConfiguration {

    @Value("#{${circuitbreaker.instances}}")
    private Map<String, Map<String, String>> circuitBreakerProperties;

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();

        // Configurar cada Circuit Breaker com base nas propriedades
        circuitBreakerProperties.forEach((name, properties) -> {
            CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                    .failureRateThreshold(Float.parseFloat(properties.getOrDefault("failureRateThreshold", "50")))
                    .slowCallRateThreshold(Float.parseFloat(properties.getOrDefault("slowCallRateThreshold", "50")))
                    .slowCallDurationThreshold(Duration.ofMillis(Long.parseLong(properties.getOrDefault("slowCallDurationThreshold", "1000"))))
                    .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.valueOf(properties.getOrDefault("slidingWindowType", "COUNT_BASED")))
                    .waitDurationInOpenState(Duration.ofMillis(Long.parseLong(properties.getOrDefault("waitDurationInOpenState", "60000"))))
                    .minimumNumberOfCalls(Integer.parseInt(properties.getOrDefault("minimumNumberOfCalls", "5")))
                    .automaticTransitionFromOpenToHalfOpenEnabled(true)
                    .build();

            registry.circuitBreaker(name, config);
        });

        return registry;
    }

    @Bean
    public Map<String, CircuitBreaker> circuitBreakers(CircuitBreakerRegistry registry) {
        Map<String, CircuitBreaker> circuitBreakerMap = new HashMap<>();

        circuitBreakerProperties.keySet().forEach(name -> {
            CircuitBreaker circuitBreaker = registry.circuitBreaker(name);
            circuitBreakerMap.put(name, circuitBreaker);
        });

        return circuitBreakerMap;
    }
}