package com.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class AppTest {
    @Test
    public void testGreeting() {
        App classUnderTest = new App();
        assertEquals("Hello World from Java 25!", classUnderTest.getGreeting(), "Sollte übereinstimmen");
    }
}
