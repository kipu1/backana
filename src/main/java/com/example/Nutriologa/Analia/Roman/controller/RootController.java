package com.example.Nutriologa.Analia.Roman.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("API is running");
    }
}
