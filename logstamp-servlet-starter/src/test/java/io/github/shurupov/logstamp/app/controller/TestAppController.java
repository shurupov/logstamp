package io.github.shurupov.logstamp.app.controller;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestAppController {

  @GetMapping("/humans/{humanId}")
  public HumanResponse getHuman(@PathVariable("humanId") Long humanId) {
    log.info("Getting human humanId: {}", humanId);
    return new HumanResponse("Gagarin", "Yuri");
  }

  @PostMapping("/humans")
  public AddHumanResponse addHuman() {
    log.info("Adding human");
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
