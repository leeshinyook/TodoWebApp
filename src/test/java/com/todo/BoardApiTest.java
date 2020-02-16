package com.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.domain.Board;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void GET_BOARD_조회하는_테스트() throws Exception {
        mockMvc.perform(get("/api/boards")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void GET_ONE_BOARD_조회하는_테스트() throws Exception {
        mockMvc.perform(get("/api/boards/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void POST_BOARD_삽입하는_테스트() throws Exception {
        Board board = Board.builder()
                .content("Test")
                .build();

        mockMvc.perform(post("/api/boards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(board)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
    @Test
    public void Delete_BOARD_삭제하는_테스트() throws Exception {
        Board board = Board.builder()
                .content("Test")
                .build();

        mockMvc.perform(post("/api/boards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(board)))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(delete("/api/boards/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void PUT_BOARD_수정하는_테스트() throws Exception {
        Board board = Board.builder()
                .content("Test")
                .build();

        mockMvc.perform(post("/api/boards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(board)))
                .andDo(print())
                .andExpect(status().isCreated());

        Board modifiedBoard = Board.builder()
                .content("modifiedTest")
                .build();
        mockMvc.perform(put("/api/boards/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(modifiedBoard)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
