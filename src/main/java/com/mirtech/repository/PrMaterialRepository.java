package com.mirtech.repository;

import com.mirtech.entity.PrMaterial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrMaterialRepository extends JpaRepository<PrMaterial, Long> {

    List<PrMaterial> findAllByOrderByCreatedAtDesc();

}
