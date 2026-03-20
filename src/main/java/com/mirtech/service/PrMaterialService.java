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

}
