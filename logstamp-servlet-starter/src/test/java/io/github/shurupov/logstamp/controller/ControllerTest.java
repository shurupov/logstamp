package io.github.shurupov.logstamp.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class ControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void contextLoads() {
  }

  @Test
  void whenGetHuman_thenStampPassed() throws Exception {
    mockMvc.perform(get("/humans/{humanId}", 1))
        .andExpect(status().isOk());
  }
}
