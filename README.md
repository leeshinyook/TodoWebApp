# TodoWebApp

> TodoList를 만들어보자!
>
> 보통, 간단하게 바닐라 JS로도 충분히 만들 수 있는 투두리스트이다.
>
> 하지만, 주말에 휴식도 취하면서 게시판도 만들어보면서 투두리스트도  좀 더 오버(?)해서 만들어 보고싶었다.

- Main : Vue.js, SpringBoot, H2
- Module : axios, Spring JPA, bootstrap-vue, bootstrap, lombok



## Scaffolding

> 전체적인 모습은 SpringBoot를 RESTful API(CRUD)로 사용하고, Vue.js를 SPA 싱글페이지 애플리케이션으로 페이지를
>
> 제작해 볼 것이다. 그리고 이를 axios를 통해 통신하기로 한다!

### H2 데이터베이스

> 간단하게 투두리스트를 구현할 것 이기에, 무거운 영구 데이터베이스의 사용을 하지 않았다. 
>
> H2라는 인 메모리 데이터베이스를 사용하여, 가볍게 진행하도록했다.



### JPA

- ORM(객체 관계 매핑)

  - 자바를 백엔드로 하기 때문에, 데이터베이스가 가지는 관계형 패러다임과

    자바의 객체지향 패러다임에 충돌이 있다. 이를 저장하고 조회 등 과정에서 문제가 발생할 수 있는데,

    이런 객체 관계 매핑을 통해 해결할 수 있다. 내가 직접 쿼리문을 짜지않아도 디비에 접근이 가능하다.

~~~java
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

~~~

> 이런식으로 간단하게 구현할 수 있다.
>
> `Lombok` 을 활용하면, 기본적인 게터나 세터, 생성까지 해주는 좋은 플러그인인데, 확실히 코드의 양이 많이 줄게 되었다.





## API Test

> 이번에 방학때, 많은 책을 읽었는데 하나같이 강조하고 하는 것은 테스트에 관한 내용이었다.
>
> API를 제작했으면 그에 따른 테스트도 실행되어야한다. 나도 열심히 테스트코드를 작성하다보니, 평소에는 console.log(),
>
> print().. Postman을 통해 열심히 확인하면서 제작했는데, 그럴 필요가 없었다.
>
> 검증을 위해 테스트케이스를 열심히 작성했고, 이를 통과하기 위한 코드를 짜고, 그냥 프론트에서 불러오기만 하면되는 것 이었다. 

- 순서
  - API테스트코드 작성 -> API작성

~~~java
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

~~~

- MockMVC를 이용해서 목을 통해 RESTAPI의 테스트를 진행 할 수 있었다. 모든 테스트가 통과후 프론트 개발단으로 넘어간다.

![스크린샷 2020-02-17 오전 12 48 57](https://user-images.githubusercontent.com/55838461/74607808-57f91880-511f-11ea-9b84-494eb035a538.png)

> intelliJ를 이용하면 너무 편하다..

## Front-end

> 투두리스트의 CRUD를 위한 테스트를 통과한 RESTAPI만들어 두었고, 편안하게 진행하도록한다



- axios를 설치하고 라이프사이클을 이용해 리스트를 불러오자.
- 각 버튼에 api를 달아서 역할을 잘 수행하도록 하자.



## 추가

![2020-02-16 23 42 16](https://user-images.githubusercontent.com/55838461/74607943-74498500-5120-11ea-8dac-36726381d670.gif)

## 삭제

![2020-02-16 23 42 45](https://user-images.githubusercontent.com/55838461/74607956-89beaf00-5120-11ea-9c40-700a47ef891f.gif)

## 수정

![2020-02-16 23 43 32](https://user-images.githubusercontent.com/55838461/74607961-8d523600-5120-11ea-9e72-b3479808369d.gif)

## 완료

![2020-02-16 23 43 55](https://user-images.githubusercontent.com/55838461/74607964-8e836300-5120-11ea-97ee-20209ecf2d5e.gif)



# 마무리

> 요즘 스프링에 빠져있다. Node.js도 정말 재미있었지만, 스프링도 무엇인가 철저한 규칙과 행동에 매력이 있다.

- 아직 스프링 부트에 대해 미숙하다. 어노테이션이 어떻게 이뤄져있는지, 어떤 역할을 하는지 정확하게

  확인하면서 공부할 필요가 있을 것 같다.

- 테스트작성에 대해 미흡하다. 이 부분에 관해 조금 더 공부하고 확인해 볼 필요가 있을 것 같다.