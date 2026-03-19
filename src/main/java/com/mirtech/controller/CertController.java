package com.mirtech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cert")
public class CertController {

    @GetMapping("/iso")
    public String iso(){
        return "cert/iso";
    }

    @GetMapping("/doc")
    public String doc(){
        return "cert/doc";
    }
}
