package io.github.shurupov.logstamp.app.controller;

import java.io.IOException;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class TestAppController {

  @GetMapping("/humans/{humanId}")
  public HumanResponse getHuman(@PathVariable("humanId") Long humanId) {
    log.info("Getting human humanId: {}", humanId);
    return new HumanResponse("Gagarin", "Yuri");
  }

  @PostMapping("/humans")
  public AddHumanResponse addHuman(@RequestPart("file") MultipartFile file) throws IOException {
    log.info("uploaded file: {}", new String(file.getBytes()));
    return new AddHumanResponse(1L);
  }

  @Value
  public static class HumanResponse {
    String lastname;
    String firstName;
  }

  @Value
  public static class AddHumanResponse {
    Long humanId;
  }
}
