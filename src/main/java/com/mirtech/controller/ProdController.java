package com.mirtech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prod")
public class ProdController {

    @GetMapping("/laser")
    public String laser() { return "prod/laser"; }

    @GetMapping("/pta")
    public String pta() { return "prod/pta"; }

    @GetMapping("/spray")
    public String spray() { return "prod/spray"; }

    @GetMapping("/hydraulic")
    public String hydraulic() { return "prod/hydraulic"; }

    @GetMapping("/magnesium")
    public String magnesium() { return "prod/magnesium"; }

    @GetMapping("/aluminum")
    public String aluminum() { return "prod/aluminum"; }

    @GetMapping("/plant")
    public String plant() { return "prod/plant"; }

    @GetMapping("/robot")
    public String robot() { return "prod/robot"; }

    @GetMapping("/auto")
    public String auto() { return "prod/auto"; }

}
