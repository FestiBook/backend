package com.festibook.festibook_backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("open-api")
@Getter
@Setter
public class OpenApiProperty {

  private EventApi eventApi;

  @Getter
  @Setter
  public static class EventApi {
    private String url;
  }
}
