package com.mirtech.controller;

import com.mirtech.entity.Inquiry;
import com.mirtech.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class CsController {

    private final InquiryService inquiryService;

    @GetMapping("/inquiry")
    public String submitInquiry(@ModelAttribute Inquiry inquiry, RedirectAttributes redirectAttributes){

        try{
            inquiryService.submit(inquiry);
            redirectAttributes.addFlashAttribute("successMsg", "문의가 정상적으로 접수되었습니다. 빠른 시일 내에 연락드리겠습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "문의 접수 중 오류가 발생했습니다. 다시 시도해 주세요.");
        }
        return "redirect:/cs/inquiry";
    }

}
