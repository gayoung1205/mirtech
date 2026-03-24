package com.mirtech.repository;


import com.mirtech.entity.Inquiry;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findTop5ByOrderByCreatedAtDesc();

    Page<Inquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Inquiry> findByIsReadOrderByCreatedAtDesc(boolean isRead, Pageable pageable);

    long countByIsRead(boolean isRead);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
