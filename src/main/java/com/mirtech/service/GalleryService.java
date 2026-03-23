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

    public long countAll(){
        return galleryRepository.count();
    }

    public Page<Gallery> getAdminList(int page) {
        return galleryRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, 16));
    }

    public void save(Gallery gallery) {
        galleryRepository.save(gallery);
    }

    public void delete(Long id){
        galleryRepository.deleteById(id);
    }

}
