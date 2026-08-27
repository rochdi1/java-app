package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    @Test
    public void testGreeting() {
        App app = new App();
        assertEquals("Hello World from local DevOps Pipeline!", app.getGreeting());
    }
}
