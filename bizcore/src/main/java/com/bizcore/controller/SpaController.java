package com.bizcore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle Single Page Application (SPA) routing.
 * Forwards all /app/* requests to the React app's index.html
 */
@Controller
public class SpaController {

    @GetMapping("/")
    public String root() {
        return "redirect:/login.html";
    }

    @GetMapping("/app")
    public String appRoot() {
        return "forward:/app/index.html";
    }

    @GetMapping("/app/")
    public String appRootSlash() {
        return "forward:/app/index.html";
    }

    @GetMapping("/app/{path:[^\\.]*}")
    public String appPath() {
        return "forward:/app/index.html";
    }

    @GetMapping("/app/{path:[^\\.]*}/{subpath:[^\\.]*}")
    public String appSubPath() {
        return "forward:/app/index.html";
    }
}
