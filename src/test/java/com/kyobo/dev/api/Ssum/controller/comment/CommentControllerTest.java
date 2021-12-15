package com.kyobo.dev.api.Ssum.controller.comment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kyobo.dev.api.Ssum.model.request.comment.CommentRequestDto;
import com.kyobo.dev.api.Ssum.model.request.comment.NestedCommentRequestDto;
import com.kyobo.dev.api.Ssum.model.request.user.LoginDto;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@NoArgsConstructor
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)  // Test Cash 의 순서를 @DisplayName 어노테이션 순서로 실행.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Test 를 진행하며 accessToken, refreshToken 같은 공유 자원을 모든 메소드가 공유하도록 설정.
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    String accessToken;
    String commentId;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    public void beforeEach() {
        // MockMvc 테스트 시 CharSet을 UTF-8로 설정하기 위해 filter 추가. (한글 깨짐 방지)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity())  // 스프링 시큐리티를 스프링 MVC 테스트와 통합할 때 필요한 모든 초기 세팅을 수행한다. 반드시 설정!
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())  // 모든 테스트에 대한 결과를 출력
                .build();

        objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();
    }

    @DisplayName("1.로그인 테스트")
    @Test
    public void loginTest() throws Exception {

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test1@naver.com");
        loginDto.setPassword("12345");

        String content = objectMapper.writeValueAsString(loginDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))
                .andReturn();

        // MvcResult 객체 통해서 accessToken 얻어오기.
        String responseString = result.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(responseString, new TypeReference<Map<String, Object>>() {});
        Map<String, String> mapData = (Map<String, String>) map.get("data");
        accessToken = mapData.get("accessToken");
    }

    @DisplayName("2.댓글 작성 테스트")
    //@Transactional
    @Test
    public void addCommentTest() throws Exception {

        String pathVariable = "1";

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents("테스트 댓글입니다.");

        String content = objectMapper.writeValueAsString(commentRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/board/comment/" + pathVariable)
                        .header("X-AUTH-TOKEN", accessToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))
                .andReturn();

        // MvcResult 객체 통해서 commentId 얻어오기.
        String responseString = result.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(responseString, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> mapData = (Map<String, Object>) map.get("data");
        commentId = Integer.toString((Integer) mapData.get("commentId"));
    }

    @DisplayName("3.댓글 조회 테스트")
    //@Transactional
    @Test
    public void searchCommentTest() throws Exception {

        String pathVariable = "1";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/board/comment/" + pathVariable))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0));
    }

    @DisplayName("4.댓글 수정 테스트")
    //@Transactional
    @Test
    public void updateCommentTest() throws Exception {

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents("수정 댓글입니다.");

        String content = objectMapper.writeValueAsString(commentRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/board/comment/" + commentId)
                        .header("X-AUTH-TOKEN", accessToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.contents").value("수정 댓글입니다."));

    }

    @DisplayName("4.1 대댓글 추가 테스트")
    //@Transactional
    @Test
    public void addNestedCommentTest() throws Exception {

        NestedCommentRequestDto nestedCommentRequestDto = new NestedCommentRequestDto();
        nestedCommentRequestDto.setContents("대댓글입니다.");

        String content = objectMapper.writeValueAsString(nestedCommentRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/board/nested/comment/" + commentId)
                        .header("X-AUTH-TOKEN", accessToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.contents").value("대댓글입니다."));

    }

    @DisplayName("5.댓글 삭제 테스트")
    //@Transactional
    @Test
    public void deleteCommentTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/board/comment/" + commentId)
                        .header("X-AUTH-TOKEN", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0));
    }
}
