package com.mirtech.repository;


import com.mirtech.entity.Inquiry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findTop5ByOrderByCreatedAtDesc();
}
