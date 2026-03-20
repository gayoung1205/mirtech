package com.mirtech.repository;

import com.mirtech.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByBoardTypeOrderByCreatedAtDesc(String BoardType, Pageable pageable);

    Page<Board> findByBoardTypeAndTitleContainingOrderByCreatedAtDesc(String boardType, String title, Pageable pageable);

}
