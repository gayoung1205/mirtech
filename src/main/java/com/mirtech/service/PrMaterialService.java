package com.mirtech.service;

import com.mirtech.entity.PrMaterial;
import com.mirtech.repository.PrMaterialRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrMaterialService {

    private final PrMaterialRepository prMaterialRepository;

    public List<PrMaterial> getAll(){
        return prMaterialRepository.findAllByOrderByCreatedAtDesc();
    }

    public PrMaterial getById(Long id) {
        return prMaterialRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("자료를 찾을 수 없습니다."));
    }

    public void save(PrMaterial material){
        prMaterialRepository.save(material);
    }

    public void delete(Long id) {
        prMaterialRepository.deleteById(id);
    }

}
