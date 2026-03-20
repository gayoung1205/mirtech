package com.mirtech.controller;

import com.mirtech.entity.Board;
import com.mirtech.entity.Inquiry;
import com.mirtech.service.BoardService;
import com.mirtech.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class CsController {

    private final InquiryService inquiryService;
    private final BoardService boardService;

    @GetMapping("/notice")
    public String notice(Model model,
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "0") int page) {
        Page<Board> boards = boardService.getList("NOTICE", keyword, page);
        model.addAttribute("boards", boards);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        return "cs/notice";
    }

    @GetMapping("/notice/{id}")
    public String noticeDetail(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.getDetail(id));
        return "cs/notice-detail";
    }

    // ── 기술자료실 ──
    @GetMapping("/reference")
    public String reference(Model model,
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "0") int page) {
        Page<Board> boards = boardService.getList("REFERENCE", keyword, page);
        model.addAttribute("boards", boards);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        return "cs/reference";
    }

    @GetMapping("/reference/{id}")
    public String referenceDetail(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.getDetail(id));
        return "cs/reference-detail";
    }

    @GetMapping("/inquiry")
    public String inquiry(Model model){
        model.addAttribute("inquiry", new Inquiry());
        return "cs/inquiry";
    }

    @PostMapping("/inquiry")
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
