package org.example.circuitbreakerlib;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExampleServiceTest {

    @Autowired
    private ExampleService exampleService;

    @Test
    public void testFallback() {
        String response = exampleService.performOperation();
        assertEquals("Fallback response due to: Simulating failure", response);
    }
}