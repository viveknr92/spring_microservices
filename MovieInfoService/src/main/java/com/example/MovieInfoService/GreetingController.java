package com.example.MovieInfoService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("configclient")
@RefreshScope
public class GreetingController {
    @Value("${my.greeting: default value}")
    String greetingMessage;

    @GetMapping("greeting")
    public String getGreetingMessage() {
        return greetingMessage;
    }
}
