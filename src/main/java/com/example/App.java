package com.example;

public class App {
    public String getGreeting() {
        return "Hello World from local DevOps Pipeline!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
