package com.cod.AniBirth.animal;

import com.cod.AniBirth.animal.service.AnimalService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AnimalController {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AnimalService animalService;

    @Test
    @DisplayName("POST/animal/list 는 리스트 요청 url이다.")
    void t1() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/animal/list")
                                .content("""
                                        {
                                            "name": "복순이",
                                            "haircolor": "brown"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());

        MvcResult mvcResult = resultActions.andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        // 전체 응답을 출력
        System.out.println("Response: " + response.getContentAsString());

        // Authentication 헤더 출력
        String authentication = response.getHeader("Authentication");
        System.out.println("authentication : " + authentication);

        assertThat(authentication).isNotEmpty();

    }

    @Test
    @DisplayName("로그인 토큰 발급 검증")
    void t2() throws Exception {

    }
}
