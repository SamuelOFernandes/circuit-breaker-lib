package org.example.circuitbreakerlib;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CircuitBreakerConfigurationTest {

    @InjectMocks
    private CircuitBreakerConfiguration circuitBreakerConfiguration;

    @Mock
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCircuitBreakerRegistryCreation() {
        // Configurar propriedades simuladas
        Map<String, Map<String, String>> properties = new HashMap<>();
        Map<String, String> service1Props = new HashMap<>();
        service1Props.put("failureRateThreshold", "50");
        service1Props.put("slowCallRateThreshold", "50");
        service1Props.put("slowCallDurationThreshold", "1000");
        service1Props.put("slidingWindowType", "TIME_BASED");
        service1Props.put("waitDurationInOpenState", "5000");
        service1Props.put("minimumNumberOfCalls", "5");

        properties.put("service1", service1Props);

        // Definir as propriedades no CircuitBreakerConfiguration
        ReflectionTestUtils.setField(circuitBreakerConfiguration, "circuitBreakerProperties", properties);

        // Criar o registry e verificar
        CircuitBreakerRegistry registry = circuitBreakerConfiguration.circuitBreakerRegistry();
        assertNotNull(registry);

        // Verificar se o Circuit Breaker foi configurado corretamente
        CircuitBreaker circuitBreaker = registry.circuitBreaker("service1");
        CircuitBreakerConfig config = circuitBreaker.getCircuitBreakerConfig();

        assertEquals(50, config.getFailureRateThreshold());
        assertEquals(50, config.getSlowCallRateThreshold());
        assertEquals(Duration.ofMillis(1000), config.getSlowCallDurationThreshold());
        assertEquals(CircuitBreakerConfig.SlidingWindowType.TIME_BASED, config.getSlidingWindowType());
        assertEquals(5, config.getMinimumNumberOfCalls());
    }

    @Test
    void testCircuitBreakersBeanCreation() {
        // Configurar propriedades simuladas
        Map<String, Map<String, String>> properties = new HashMap<>();
        Map<String, String> service1Props = new HashMap<>();
        service1Props.put("failureRateThreshold", "50");
        service1Props.put("slowCallRateThreshold", "50");
        service1Props.put("slowCallDurationThreshold", "1000");
        service1Props.put("slidingWindowType", "TIME_BASED");
        service1Props.put("waitDurationInOpenState", "5000");
        service1Props.put("minimumNumberOfCalls", "5");

        properties.put("service1", service1Props);

        // Definir as propriedades no CircuitBreakerConfiguration
        ReflectionTestUtils.setField(circuitBreakerConfiguration, "circuitBreakerProperties", properties);

        // Criar o registry simulado
        when(circuitBreakerRegistry.circuitBreaker("service1")).thenReturn(CircuitBreaker.ofDefaults("service1"));

        Map<String, CircuitBreaker> circuitBreakers = circuitBreakerConfiguration.circuitBreakers(circuitBreakerRegistry);
        assertNotNull(circuitBreakers);
        assertNotNull(circuitBreakers.get("service1"));
    }
}