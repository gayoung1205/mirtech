package com.mirtech.service;

import com.mirtech.entity.Inquiry;
import com.mirtech.repository.InquiryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

//@Service
//@RequiredArgsConstructor
//public class InquiryService {
//
//    private final InquiryRepository inquiryRepository;
//    private final JavaMailSender mailSender;
//
//    @Value("${app.mail.admin}")
//    private String adminEmail;
//
//    public void submit(Inquiry inquiry){
//
//        inquiryRepository.save(inquiry);
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(adminEmail);
//        message.setSubject("[미르테크 견적문의]" + inquiry.getBizType() + " - " + inquiry.getName());
//        message.setText(
//            "새로운 견적문의가 접수되었습니다.\n\n" +
//                "■ 사업분야 : " + inquiry.getBizType() + "\n" +
//                "■ 이름     : " + inquiry.getName() + "\n" +
//                "■ 이메일   : " + inquiry.getEmail() + "\n" +
//                "■ 연락처   : " + inquiry.getPhone() + "\n\n" +
//                "■ 문의 내용\n" + inquiry.getContent()
//        );
//        mailSender.send(message);
//    }
//}

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public void submit(Inquiry inquiry) {
        // DB 저장만 (메일 발송은 추후 추가)
        inquiryRepository.save(inquiry);
    }

    public long countAll() {
        return inquiryRepository.count();
    }

    public java.util.List<Inquiry> getRecent5(){
        return inquiryRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public Page<Inquiry> getAdminList(String status, int page) {
        PageRequest pageable = PageRequest.of(page, 10);
        if ("unread".equals(status)) {
            return inquiryRepository.findByIsReadOrderByCreatedAtDesc(false, pageable);
        } else if ("read".equals(status)) {
            return inquiryRepository.findByIsReadOrderByCreatedAtDesc(true, pageable);
        }
        return inquiryRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Inquiry getById(Long id) {
        return inquiryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("문의를 찾을 수 없습니다."));
    }

    public void updateReadStatus(Long id, boolean isRead) {
        Inquiry inq = getById(id);
        inq.setRead(isRead);
        inquiryRepository.save(inq);
    }

    public void delete(Long id) {
        inquiryRepository.deleteById(id);
    }

    public long countUnread() {
        return inquiryRepository.countByIsRead(false);
    }

    public long countThisMonth() {
        LocalDateTime start = LocalDateTime.now()
            .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end   = LocalDateTime.now();
        return inquiryRepository.countByCreatedAtBetween(start, end);
    }

    public long countLastMonth() {
        LocalDateTime start = LocalDateTime.now().minusMonths(1)
            .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end   = LocalDateTime.now()
            .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return inquiryRepository.countByCreatedAtBetween(start, end);
    }
}
