package com.mirtech.controller;

import com.mirtech.entity.Inquiry;
import com.mirtech.service.InquiryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final InquiryService inquiryService;

    public HomeController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("inquiry", new Inquiry());
        return "home/index";
    }

    @PostMapping("/inquiry")
    public String submitFromHome(@ModelAttribute Inquiry inquiry, RedirectAttributes redirectAttributes){
        try {
            inquiryService.submit(inquiry);
            redirectAttributes.addFlashAttribute("successMsg", "문의가 정상적으로 접수되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "오류가 발생했습니다. 다시 시도해 주세요.");
        }
        return "redirect:/#inquiry";
    }
}
