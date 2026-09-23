package io.github.shurupov.logstamp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.github.shurupov.logstamp.app.controller.TestAppController;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class ControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private ListAppender<ILoggingEvent> listAppender;

  @BeforeEach
  void attachAppender() {
    Logger logger = (Logger) LoggerFactory.getLogger(TestAppController.class);
    listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);
  }

  @AfterEach
  void detachAppender() {
    Logger logger = (Logger) LoggerFactory.getLogger(TestAppController.class);
    logger.detachAppender(listAppender);
  }

  @Test
  void contextLoads() {
  }

  @Test
  void whenGetHuman_thenStampPassed() throws Exception {
    mockMvc.perform(get("/humans/{humanId}", 1)
            .header("x-stamp-human-getting-id", "first")
        )
        .andExpect(status().isOk());

    ILoggingEvent targetEvent = listAppender.list.stream()
        .filter(e -> "Getting human humanId: 1".equals(e.getFormattedMessage()))
        .findFirst()
        .orElseThrow();
    assertThat(targetEvent.getMDCPropertyMap())
        .containsEntry("humanGettingId", "first");
  }

  @Test
  void whenUploadHuman_thenStampPassed() throws Exception {
    mockMvc.perform(multipart("/humans")
            .file("file", "uploaded content".getBytes(StandardCharsets.UTF_8))
            .header("x-stamp-human-creation-id", "second")
        )
        .andExpect(status().isOk());

    ILoggingEvent targetEvent = listAppender.list.stream()
        .filter(e -> "uploaded file: uploaded content".equals(e.getFormattedMessage()))
        .findFirst()
        .orElseThrow();
    assertThat(targetEvent.getMDCPropertyMap())
        .containsEntry("humanCreationId", "second");
  }
}
