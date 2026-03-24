package com.mirtech.controller;

import com.mirtech.entity.Board;
import com.mirtech.entity.Gallery;
import com.mirtech.entity.Inquiry;
import com.mirtech.entity.PrMaterial;
import com.mirtech.service.BoardService;
import com.mirtech.service.GalleryService;
import com.mirtech.service.InquiryService;
import com.mirtech.service.PrMaterialService;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final PrMaterialService prMaterialService;
    private final GalleryService galleryService;

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

    @GetMapping("/pr")
    public String pr(Model model){
        model.addAttribute("materials", prMaterialService.getAll());
        return "cs/pr";
    }

    @GetMapping("/gallery")
    public String gallery(Model model,
                        @RequestParam(defaultValue = "0") int page){
        Page<Gallery> galleries = galleryService.getList(page);
        model.addAttribute("galleries", galleries);
        model.addAttribute("currentPage", page);
        return "cs/gallery";
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

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long id) {
        try {
            Board board = boardService.getById(id);

            if (board.getFilePath() == null) {
                return ResponseEntity.notFound().build();
            }

            String relativePath = board.getFilePath()
                .replaceFirst("^/uploads/", "");
            File file = Paths.get(uploadDir, relativePath)
                .toAbsolutePath().toFile();

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            String encodedName = URLEncoder.encode(board.getFileName(),
                    StandardCharsets.UTF_8)
                .replace("+", "%20");

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename*=UTF-8''" + encodedName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(new InputStreamResource(new FileInputStream(file)));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/pr/download/{id}")
    public ResponseEntity<InputStreamResource> prDownload(@PathVariable Long id) {
        try {
            PrMaterial mat = prMaterialService.getById(id);

            String relativePath = mat.getFilePath()
                .replaceFirst("^/uploads/", "");
            File file = Paths.get(uploadDir, relativePath)
                .toAbsolutePath().toFile();

            if (!file.exists()) return ResponseEntity.notFound().build();

            String ext = mat.getFileName()
                .substring(mat.getFileName().lastIndexOf("."));
            String downloadName = mat.getTitle() + ext;

            String encodedName = URLEncoder.encode(downloadName, StandardCharsets.UTF_8)
                .replace("+", "%20");

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename*=UTF-8''" + encodedName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(new InputStreamResource(new FileInputStream(file)));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
