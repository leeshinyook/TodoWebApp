package com.todo.domain;

import com.todo.domain.enums.BoardCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
public class Board implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @Builder
    public Board(String content, BoardCategory boardCategory, LocalDateTime createdDate) {
        this.content = content;
        this.boardCategory = boardCategory;
        this.createdDate = createdDate;
    }

    public void update(Board board) {
        this.content = board.getContent();
        this.boardCategory = board.getBoardCategory();
        this.updatedDate = LocalDateTime.now();
    }

}
