package com.todo.controller;

import com.todo.domain.Board;
import com.todo.repository.BoardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards")
public class BoardRestController {

     private BoardRepository boardRepository;

     public BoardRestController(BoardRepository boardRepository) {
         this.boardRepository = boardRepository;
     }

     @GetMapping
    public ResponseEntity<?> getBoards() {
         List<Board> boards = boardRepository.findAll();
         return ResponseEntity.ok(boards);
     }

     @GetMapping("/{id}")
     public ResponseEntity<?> getOneBoard(@PathVariable("id") Long id) {
         Optional<Board> board = boardRepository.findById(id);
         return ResponseEntity.ok(board);
     }

     @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteBoard(@PathVariable("id") Long id) {
         Board board = boardRepository.getOne(id);
//         Optional<Board/* /*/> board = boardRepository.findById(id);
         boardRepository.deleteById(board.getId());
         return new ResponseEntity<>("", HttpStatus.OK);
     }

     @PutMapping("/{id}")
    public ResponseEntity<?> putBoard(@PathVariable("id") Long id, @RequestBody Board board) {
         Board persistBoard = boardRepository.getOne(id);
         persistBoard.update(board);
         boardRepository.save(persistBoard);
         return new ResponseEntity<>("", HttpStatus.OK);
     }

     @PostMapping
    public ResponseEntity<?> postBoard(@RequestBody Board board) {
         board.setCreatedDate(LocalDateTime.now());
         boardRepository.save(board);
         return new ResponseEntity<>("", HttpStatus.CREATED);
     }

}
