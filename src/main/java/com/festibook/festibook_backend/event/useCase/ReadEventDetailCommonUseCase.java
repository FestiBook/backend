package com.festibook.festibook_backend.event.useCase;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festibook.festibook_backend.configuration.OpenApiProperty;
import com.festibook.festibook_backend.core.util.OkhttpJsonRequest;
import com.festibook.festibook_backend.external.event.dto.EventDetailCommonResponse;
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
public class ReadEventDetailCommonUseCase {

  private final OpenApiProperty openApiProperty;
  private final ObjectMapper objectMapper = new ObjectMapper().configure(
      DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  @Value("${open-api.service-key}")
  private String OPEN_API_SERVICE_KEY;

  public ReadEventDetailCommonUseCase.ReadEventDetailCommonResponse execute(
      ReadEventDetailCommonUseCase.ReadEventDetailCommonRequest request) {
    OkhttpJsonRequest okhttpJsonRequest = toJsonRequest(request);
    Response response = okhttpJsonRequest.request();
    EventDetailCommonResponse eventDetailCommonResponse = null;
    try {
      eventDetailCommonResponse = objectMapper.readValue(response.body().string(),
          EventDetailCommonResponse.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create OkhttpJsonRequest", e);
    }
    return ReadEventDetailCommonUseCase.ReadEventDetailCommonResponse.builder()
        .eventDetailCommonResponse(eventDetailCommonResponse)
        .build();
  }

  public OkhttpJsonRequest toJsonRequest(
      ReadEventDetailCommonUseCase.ReadEventDetailCommonRequest request) {
    if (openApiProperty == null || openApiProperty.getEventDetailApi() == null
        || openApiProperty.getEventDetailApi().getCommonUrl() == null) {
      return null;
    }
    String queryString = "?numOfRows=1"
        + "&pageNo=1"
        + "&MobileOS=ETC"
        + "&MobileApp=AppTest"
        + "&_type=json"
        + "&defaultYN=Y"
        + "&overviewYN=Y"
        + "&contentId="
        + request.getEventId()
        + "&serviceKey="
        + OPEN_API_SERVICE_KEY;
    try {
      OkhttpJsonRequest okhttpJsonRequest = new OkhttpJsonRequest(
          openApiProperty.getEventDetailApi().getCommonUrl() + queryString, null, HttpMethod.GET);
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
  public static class ReadEventDetailCommonResponse {

    private EventDetailCommonResponse eventDetailCommonResponse;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadEventDetailCommonRequest {

    private Integer eventId;
  }

}

