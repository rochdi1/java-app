package com.example;
public class App {
    public String getGreeting() {
        return "Hello World from Java 25! I am running on Java version: " + System.getProperty("java.version") +" Abdel";
    }
    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
