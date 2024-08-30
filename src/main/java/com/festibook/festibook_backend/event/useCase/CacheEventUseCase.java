package com.festibook.festibook_backend.event.useCase;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festibook.festibook_backend.configuration.OpenApiProperty;
import com.festibook.festibook_backend.core.util.OkhttpJsonRequest;
import com.festibook.festibook_backend.external.event.dto.EventResponse;
import com.festibook.festibook_backend.external.event.dto.EventResponse.Item;
import java.util.List;
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
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class CacheEventUseCase {

  private final OpenApiProperty openApiProperty;

  private final ObjectMapper objectMapper = new ObjectMapper().configure(
      DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public CacheEventResponse execute(CacheEventRequest request) {
    OkhttpJsonRequest okhttpJsonRequest = toJsonRequest(request);
    Response response = okhttpJsonRequest.request();
    EventResponse eventResponse = null;
    try {
      eventResponse = objectMapper.readValue(response.body().string(), EventResponse.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create OkhttpJsonRequest", e);
    }
    return CacheEventResponse.builder()
        .items(eventResponse.getResponse().getBody().getItems().getItem())
        .build();
  }

  private CacheEventRequest toResult(Response response) {
    return CacheEventRequest.builder().build();
  }

  public OkhttpJsonRequest toJsonRequest(CacheEventRequest request) {
    if (openApiProperty == null || openApiProperty.getEventApi() == null
        || openApiProperty.getEventApi().getUrl() == null) {
      return null;
    }
    String queryString = "?numOfRows=50"
        + "&pageNo=1"
        + "&MobileOS=ETC"
        + "&MobileApp=AppTest"
        + "&_type=json"
        + "&listYN=Y"
        + "&arrange=A"
        + "&eventStartDate="
        + request.getEventStartDate()
        + "&serviceKey=";
    try {
      OkhttpJsonRequest okhttpJsonRequest = new OkhttpJsonRequest(
          openApiProperty.getEventApi().getUrl() + queryString, null, HttpMethod.GET);
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
  public static class CacheEventResponse {

    List<Item> items;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class CacheEventRequest {

    private String eventStartDate;
  }

}
