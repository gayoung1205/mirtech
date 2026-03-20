package com.mirtech.service;

import com.mirtech.entity.Gallery;
import com.mirtech.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository;

    public Page<Gallery> getList(int page){
        return galleryRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, 9));
    }

}
