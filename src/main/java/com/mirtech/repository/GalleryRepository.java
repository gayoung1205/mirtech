package com.mirtech.repository;

import com.mirtech.entity.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    Page<Gallery> findAllByOrderByCreatedAtDesc(Pageable pageable);

}