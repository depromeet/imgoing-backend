package org.imgoing.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthCheckController {
    @GetMapping("")
    @ResponseBody
    public String healthCheck() {
        String message = "Welcome to imgoing API! :)";
        return message;
    }
}
