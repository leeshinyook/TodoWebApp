package com.todo.domain;

import com.todo.domain.enums.BoardCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@Table
@Setter
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
    public Board(String content, BoardCategory boardCategory, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.boardCategory = boardCategory;
    }

    public void update(Board board) {
        this.content = board.getContent();
        this.boardCategory = board.getBoardCategory();
        this.updatedDate = LocalDateTime.now();
    }

}
