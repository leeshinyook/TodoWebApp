package com.todo.repository;

import com.todo.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findById(String id);
    Board deleteById(String id);
}
