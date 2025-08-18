package com.cositems.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthChecker {

    @GetMapping
    public String healthChecker() {
        return "Estou saudável!!";
    }
    
}
