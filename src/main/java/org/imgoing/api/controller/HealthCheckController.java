package org.imgoing.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class HealthCheckController {
    @GetMapping("")
    public ResponseEntity<String> healthCheck() {
        String message = "Welcome to imgoing API! :)";
        return ResponseEntity.status(200).body(message);
    }

    @GetMapping("/policy")
    public ModelAndView privacyPolicy() {
        return new ModelAndView("privacy-policy");
    }
}