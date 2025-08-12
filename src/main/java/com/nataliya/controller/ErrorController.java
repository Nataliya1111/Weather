package com.nataliya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    private static final String ERROR_VIEW = "error";

    @GetMapping
    public String showErrorPage() {
        return ERROR_VIEW;
    }

}
