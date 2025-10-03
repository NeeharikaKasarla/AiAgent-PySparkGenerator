package com.agent.ai_agent_service.api;

import com.agent.ai_agent_service.service.PySparkAgentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PySparkController.class)
class PySparkControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean PySparkAgentService service;

    @Test
    void testGenerate() throws Exception {
        Mockito.when(service.generate(Mockito.any())).thenReturn(
                new PySparkGenerateResponse("print('hello')", List.of("Step1"), "LOW", "safe")
        );

        mockMvc.perform(post("/v1/pyspark/generate")
                        .contentType("application/json")
                        .content("{\"requirement\":\"test\",\"filePath\":\"f.csv\",\"fileFormat\":\"csv\",\"schema\":{\"c1\":\"string\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pyspark_code").value("print('hello')"));
    }
}

