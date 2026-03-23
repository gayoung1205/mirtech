package com.mirtech.service;

import com.mirtech.entity.Board;
import com.mirtech.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> getList(String boardType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        if (keyword != null && !keyword.isBlank()) {
            return boardRepository
                .findByBoardTypeAndTitleContainingOrderByCreatedAtDesc(boardType, keyword, pageable);
        }
        return boardRepository
            .findByBoardTypeOrderByCreatedAtDesc(boardType, pageable);
    }

    public Board getDetail(Long id) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.setViews(board.getViews() + 1);
        return boardRepository.save(board);
    }

    public long countByType(String boardType){
        return boardRepository.countByBoardType(boardType);
    }

    public Page<Board> getAdminList(String boardType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        if (keyword != null && !keyword.isBlank()) {
            return boardRepository
                .findByBoardTypeAndTitleContainingOrderByCreatedAtDesc(boardType, keyword, pageable);
        }
        return boardRepository.findByBoardTypeOrderByCreatedAtDesc(boardType, pageable);
    }

    public void save(Board board) {
        boardRepository.save(board);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Board getById(Long id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

}
