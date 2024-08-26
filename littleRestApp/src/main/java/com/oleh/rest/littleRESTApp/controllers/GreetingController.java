package com.oleh.rest.littleRESTApp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class GreetingController {
    private final String defaultGreetingMsg = "Hello, dear visitor. Make yourself comfortable.";
    private String greetingMsg = defaultGreetingMsg;

    @GetMapping("/greet")
    public String getGreeting() {
        log.debug("Going to return a greeting: \"{}\"", greetingMsg);
        return greetingMsg;
    }

    @PostMapping("/greet")
    public String postUserGreeting(@RequestBody String userGreeting) {
        log.debug("Got a User greeting: \"{}\"", userGreeting);
        return String.format("You've said: \"%s\"", userGreeting);
    }

    @PutMapping("/greet")
    public String updateGreeting(@RequestBody String message) {
        greetingMsg = message;
        log.debug("Updated greeting message to: \"{}\"", greetingMsg);
        return String.format("Updated greeting message to: \"%s\"", greetingMsg);
    }

    @DeleteMapping("/greet")
    public String delete() {
        greetingMsg = defaultGreetingMsg;
        log.debug("Greeting message reset to default");
        return "Greeting message reset to default";
    }
}
