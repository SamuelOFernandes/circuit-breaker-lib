package org.example.circuitbreakerlib;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    @CircuitBreaker(name = "defaultcircuitbreaker", fallbackMethod = "fallbackMethod")
    public String performOperation() {
        // Simulação de lógica de negócio que pode falhar
        throw new RuntimeException("Simulating failure");
    }

    public String fallbackMethod(Throwable t) {
        return "Fallback response due to: " + t.getMessage();
    }
}