package com.festibook.festibook_backend.event.useCase;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festibook.festibook_backend.configuration.OpenApiProperty;
import com.festibook.festibook_backend.core.util.OkhttpJsonRequest;
import com.festibook.festibook_backend.external.event.dto.EventDetailIntroResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReadEventDetailIntroUseCase {

  private final OpenApiProperty openApiProperty;
  private final ObjectMapper objectMapper = new ObjectMapper().configure(
      DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  @Value("${open-api.service-key}")
  private String OPEN_API_SERVICE_KEY;

  public ReadEventDetailIntroUseCase.ReadEventDetailIntroResponse execute(
      ReadEventDetailIntroUseCase.ReadEventDetailIntroRequest request) {
    OkhttpJsonRequest okhttpJsonRequest = toJsonRequest(request);
    Response response = okhttpJsonRequest.request();
    EventDetailIntroResponse eventDetailIntroResponse = null;
    try {
      eventDetailIntroResponse = objectMapper.readValue(response.body().string(),
          EventDetailIntroResponse.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create OkhttpJsonRequest", e);
    }
    return ReadEventDetailIntroUseCase.ReadEventDetailIntroResponse.builder()
        .eventDetailIntroResponse(eventDetailIntroResponse)
        .build();
  }

  public OkhttpJsonRequest toJsonRequest(
      ReadEventDetailIntroUseCase.ReadEventDetailIntroRequest request) {
    if (openApiProperty == null || openApiProperty.getEventDetailApi() == null
        || openApiProperty.getEventDetailApi().getIntroUrl() == null) {
      return null;
    }
    String queryString = "?numOfRows=1"
        + "&pageNo=1"
        + "&MobileOS=ETC"
        + "&MobileApp=AppTest"
        + "&_type=json"
        + "&contentId="
        + request.getEventId()
        + "&contentTypeId=15"
        + "&serviceKey="
        + OPEN_API_SERVICE_KEY;
    try {
      OkhttpJsonRequest okhttpJsonRequest = new OkhttpJsonRequest(
          openApiProperty.getEventDetailApi().getIntroUrl() + queryString, null, HttpMethod.GET);
      System.out.println("Request created successfully: " + request);
      return okhttpJsonRequest;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create OkhttpJsonRequest", e);
    }

  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadEventDetailIntroResponse {

    private EventDetailIntroResponse eventDetailIntroResponse;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadEventDetailIntroRequest {

    private Integer eventId;
  }

}

