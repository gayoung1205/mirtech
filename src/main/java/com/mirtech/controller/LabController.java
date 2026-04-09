package com.mirtech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lab")
public class LabController {

    @GetMapping("/intro")
    public String intro() { return "lab/intro"; }

    @GetMapping("/equipment")
    public String equipment() { return "lab/equipment"; }

    @GetMapping("/result")
    public String result() { return "lab/result"; }

    @GetMapping("/patent")
    public String patent() { return "lab/patent"; }

}
