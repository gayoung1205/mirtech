package com.mirtech.controller;

import com.mirtech.entity.Board;
import com.mirtech.entity.Gallery;
import com.mirtech.entity.Inquiry;
import com.mirtech.entity.PrMaterial;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import com.mirtech.service.BoardService;
import com.mirtech.service.GalleryService;
import com.mirtech.service.InquiryService;
import com.mirtech.service.PrMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

        long totalInquiry   = inquiryService.countAll();
        long unreadInquiry  = inquiryService.countUnread();
        long readInquiry    = totalInquiry - unreadInquiry;
        long monthlyInquiry = inquiryService.countThisMonth();
        long lastMonthInquiry = inquiryService.countLastMonth();
        long monthlyDiff    = monthlyInquiry - lastMonthInquiry;

        model.addAttribute("totalInquiryCount",   totalInquiry);
        model.addAttribute("unreadInquiryCount",  unreadInquiry);
        model.addAttribute("readInquiryCount",    readInquiry);
        model.addAttribute("monthlyInquiryCount", monthlyInquiry);
        model.addAttribute("monthlyDiff",         monthlyDiff);
        model.addAttribute("recentInquiries",     inquiryService.getRecent5());

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
                if (file.isEmpty())
                    continue;

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

    // ── 게시판 목록 ──
    @GetMapping("/board")
    public String board(Model model,
        @RequestParam(defaultValue = "NOTICE") String type,
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("activeMenu", "board");
        model.addAttribute("boards", boardService.getAdminList(type, keyword, page));
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        return "admin/board";
    }

    // ── 새 글 작성 폼 ──
    @GetMapping("/board/write")
    public String writeForm(Model model,
        @RequestParam(defaultValue = "NOTICE") String type) {
        model.addAttribute("activeMenu", "board");
        model.addAttribute("board", new Board());
        model.addAttribute("type", type);
        return "admin/board-form";
    }

    // ── 수정 폼 ──
    @GetMapping("/board/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Board board = boardService.getById(id);
        model.addAttribute("activeMenu", "board");
        model.addAttribute("board", board);
        model.addAttribute("type", board.getBoardType());
        return "admin/board-form";
    }

    // ── 저장 (신규 + 수정 공통) ──
    @PostMapping("/board/save")
    public String saveBoard(@ModelAttribute Board board,
        @RequestParam(required = false) MultipartFile attachFile,
        RedirectAttributes ra) {
        try {
            // 파일 첨부가 있을 때만 처리
            if (attachFile != null && !attachFile.isEmpty()) {
                Path dirPath = Paths.get(uploadDir, "board");
                Files.createDirectories(dirPath);

                String uuid = UUID.randomUUID().toString();
                String ext  = attachFile.getOriginalFilename()
                    .substring(attachFile.getOriginalFilename().lastIndexOf("."));
                String savedName = uuid + ext;

                attachFile.transferTo(dirPath.resolve(savedName).toAbsolutePath().toFile());
                board.setFileName(attachFile.getOriginalFilename());
                board.setFilePath("/uploads/board/" + savedName);
                board.setFileSize(attachFile.getSize());
            }

            boardService.save(board);
            ra.addFlashAttribute("successMsg", "저장되었습니다.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "저장 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/admin/board?type=" + board.getBoardType();
    }

    // ── 삭제 ──
    @PostMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable Long id,
        @RequestParam(defaultValue = "NOTICE") String type,
        RedirectAttributes ra) {
        boardService.delete(id);
        ra.addFlashAttribute("successMsg", "삭제되었습니다.");
        return "redirect:/admin/board?type=" + type;
    }

    @GetMapping("/pr")
    public String pr(Model model) {
        model.addAttribute("activeMenu", "pr");
        model.addAttribute("materials", prMaterialService.getAll());
        return "admin/pr";
    }

    @PostMapping("/pr/upload")
    public String uploadPr(@RequestParam("pdfFile") MultipartFile pdfFile,
        @RequestParam String title,
        RedirectAttributes ra) {
        try {
            Path dirPath = Paths.get(uploadDir, "pr");
            Files.createDirectories(dirPath);

            String uuid = UUID.randomUUID().toString();
            String ext  = pdfFile.getOriginalFilename()
                .substring(pdfFile.getOriginalFilename().lastIndexOf("."));
            String savedName = uuid + ext;
            File savedFile = dirPath.resolve(savedName).toAbsolutePath().toFile();

            pdfFile.transferTo(savedFile);

            String thumbName = uuid + "_thumb.jpg";
            Path thumbPath = dirPath.resolve(thumbName);
            try (PDDocument pdf = PDDocument.load(savedFile)) {
                PDFRenderer renderer = new PDFRenderer(pdf);
                BufferedImage image = renderer.renderImageWithDPI(0, 150);
                ImageIO.write(image, "JPEG", thumbPath.toFile());
            }

            PrMaterial mat = new PrMaterial();
            mat.setTitle(title);
            mat.setFileName(pdfFile.getOriginalFilename());
            mat.setFilePath("/uploads/pr/" + savedName);
            mat.setFileSize(pdfFile.getSize());
            mat.setThumbnail("/uploads/pr/" + thumbName);
            prMaterialService.save(mat);

            ra.addFlashAttribute("successMsg", "홍보자료가 등록되었습니다.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "업로드 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/admin/pr";
    }

    @PostMapping("/pr/edit/{id}")
    public String editPr(@PathVariable Long id, @RequestParam String title, RedirectAttributes ra) {
        PrMaterial mat = prMaterialService.getById(id);
        mat.setTitle(title);
        prMaterialService.save(mat);
        ra.addFlashAttribute("successMSG", "제목이 수정되었습니다.");
        return "redirect:/admin/pr";
    }

    @PostMapping("/pr/delete/{id}")
    public String deletePr(@PathVariable Long id, RedirectAttributes ra) {
        prMaterialService.delete(id);
        ra.addFlashAttribute("successMSG", "삭제되었습니다.");
        return "redirect:/admin/pr";
    }

    @GetMapping("/inquiry")
    public String inquiry(Model model,
        @RequestParam(defaultValue = "all") String status,
        @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("activeMenu", "inquiry");
        model.addAttribute("inquiries", inquiryService.getAdminList(status, page));
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("unreadCount", inquiryService.countUnread());
        return "admin/inquiry";
    }

    @GetMapping("/inquiry/{id}")
    public String inquiryDetail(@PathVariable Long id, Model model) {
        Inquiry inq = inquiryService.getById(id);
        model.addAttribute("activeMenu", "inquiry");
        model.addAttribute("inq", inq);
        model.addAttribute("unreadCount", inquiryService.countUnread());
        return "admin/inquiry-detail";
    }

    @GetMapping("/inquiry/{id}/open")
    public String openInquiry(@PathVariable Long id) {
        inquiryService.updateReadStatus(id, true);
        return "redirect:/admin/inquiry/" + id;
    }

    @PostMapping("/inquiry/{id}/status")
    public String updateStatus(@PathVariable Long id,
        @RequestParam boolean isRead,
        RedirectAttributes ra) {
        inquiryService.updateReadStatus(id, isRead);
        ra.addFlashAttribute("successMsg", isRead ? "확인 처리되었습니다." : "미확인으로 변경되었습니다.");
        return "redirect:/admin/inquiry/" + id;
    }

    @PostMapping("/inquiry/{id}/delete")
    public String deleteInquiry(@PathVariable Long id, RedirectAttributes ra) {
        inquiryService.delete(id);
        ra.addFlashAttribute("successMsg", "삭제되었습니다.");
        return "redirect:/admin/inquiry";
    }

}