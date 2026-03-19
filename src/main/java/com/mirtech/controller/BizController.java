package com.mirtech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/biz")
public class BizController {

    @GetMapping("/laser")
    public String laser() { return "biz/laser"; }

    @GetMapping("/pta")
    public String pta() { return "biz/pta"; }

    @GetMapping("/spray")
    public String spray() { return "biz/spray"; }

    @GetMapping("/hydraulic")
    public String hydraulic() { return "biz/hydraulic"; }

    @GetMapping("/magnesium")
    public String magnesium() { return "biz/magnesium"; }

    @GetMapping("/aluminum")
    public String aluminum() { return "biz/aluminum"; }

    @GetMapping("/plant")
    public String plant() { return "biz/plant"; }

    @GetMapping("/robot")
    public String robot() { return "biz/robot"; }

    @GetMapping("/auto")
    public String auto() { return "biz/auto"; }

}
