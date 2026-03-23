package com.mirtech.controller;

import com.mirtech.entity.Gallery;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import com.mirtech.service.BoardService;
import com.mirtech.service.GalleryService;
import com.mirtech.service.InquiryService;
import com.mirtech.service.PrMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final InquiryService inquiryService;
    private final BoardService boardService;
    private final GalleryService galleryService;
    private final PrMaterialService prMaterialService;

    @GetMapping("/login")
    public String login() {
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

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @GetMapping("/gallery")
    public String gallery(Model model, @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("activeMenu", "gallery");
        model.addAttribute("galleries", galleryService.getAdminList(page));
        model.addAttribute("currentPage", page);
        return "admin/gallery";
    }

    @PostMapping("/gallery/upload")
    public String uploadGallery(@RequestParam("files") MultipartFile[] files,
        @RequestParam(required = false) String title,
        RedirectAttributes ra) {
        try {
            // 절대경로로 폴더 생성
            Path dirPath = Paths.get(uploadDir, "gallery");
            Files.createDirectories(dirPath);

            int count = 0;
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String originalName = file.getOriginalFilename();
                String ext = originalName.substring(originalName.lastIndexOf("."));
                String savedName = UUID.randomUUID().toString() + ext;

                // 파일 실제 저장
                Path savePath = dirPath.resolve(savedName);
                file.transferTo(savePath.toAbsolutePath().toFile()); // ← 절대경로로 저장

                // DB 저장
                Gallery g = new Gallery();
                g.setTitle(title != null && !title.isBlank() ? title : originalName);
                g.setImageName(savedName);
                g.setImagePath("/uploads/gallery/" + savedName);
                galleryService.save(g);
                count++;
            }

            ra.addFlashAttribute("successMsg", count + "개 이미지가 업로드되었습니다.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "업로드 실패: " + e.getMessage());
            e.printStackTrace(); // 콘솔에서 상세 오류 확인
        }
        return "redirect:/admin/gallery";
    }

    @PostMapping("/gallery/delete/{id}")
    public String deleteGallery(@PathVariable Long id, RedirectAttributes ra) {
        galleryService.delete(id);
        ra.addFlashAttribute("successMsg", "삭제 완료");
        return "redirect:/admin/gallery";

    }

}