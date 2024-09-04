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

  private EventDetailApi eventDetailApi;

  @Getter
  @Setter
  public static class EventApi {

    private String url;
  }

  @Getter
  @Setter
  public static class EventDetailApi {

    private String commonUrl;

    private String introUrl;
  }
}
