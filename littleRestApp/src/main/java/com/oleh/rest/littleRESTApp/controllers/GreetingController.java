package com.oleh.rest.littleRESTApp.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GreetingController {
    private final String defaultGreetingMsg = "Hello, dear visitor. Make yourself comfortable.";
    private String greetingMsg = defaultGreetingMsg;

    @GetMapping("/greet")
    public String getGreeting() {
        return greetingMsg;
    }

    @PostMapping("/greet")
    public String getUserGreeting(@RequestBody String userGreeting) {
        return String.format("You've said: \"%s\"", userGreeting);
    }

    @PutMapping("/greet")
    public String updateGreeting(@RequestBody String message) {
        greetingMsg = message;
        return String.format("Updated greeting message to: \"%s\"", greetingMsg);
    }

    @DeleteMapping("/greet")
    public String delete() {
        greetingMsg = defaultGreetingMsg;
        return "Greeting message reset to default";
    }
}
