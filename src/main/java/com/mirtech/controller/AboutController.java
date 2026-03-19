package com.mirtech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
public class AboutController {

    @GetMapping("/greeting")
    public String greeting(){
        return "about/greeting";
    }

    @GetMapping("/history")
    public String history(){
        return "about/history";
    }

    @GetMapping("/vision")
    public String vision(){
        return "about/vision";
    }

    @GetMapping("/ci")
    public String ci(){
        return "about/ci";
    }

    @GetMapping("/org")
    public String org(){
        return "about/org";
    }

    @GetMapping("/facility")
    public String facility(){
        return "about/facility";
    }

    @GetMapping("/award")
    public String award() {
        return "about/award";
    }

    @GetMapping("/client")
    public String client() {
        return "about/client";
    }

    @GetMapping("/map")
    public String map() {
        return "about/map";
    }
}
