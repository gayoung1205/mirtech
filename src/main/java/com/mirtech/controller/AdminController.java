package com.mirtech.controller;

import org.springframework.ui.Model;
import com.mirtech.service.BoardService;
import com.mirtech.service.GalleryService;
import com.mirtech.service.InquiryService;
import com.mirtech.service.PrMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final InquiryService inquiryService;
    private final BoardService boardService;
    private final GalleryService galleryService;
    private final PrMaterialService prMaterialService;

    @GetMapping("/login")
    public String login(){
        return "/admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("activeMenu", "dashboard");
        model.addAttribute("inquiryCount", inquiryService.countAll());
        model.addAttribute("noticeCount", boardService.countByType("NOTICE"));
        model.addAttribute("referenceCount", boardService.countByType("REFERENCE"));
        model.addAttribute("galleryCount", galleryService.countAll());
        model.addAttribute("recentInquiries", inquiryService.getRecent5());
        return "admin/dashboard";
    }

}
