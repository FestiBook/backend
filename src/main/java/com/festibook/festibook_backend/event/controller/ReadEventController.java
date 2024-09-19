package com.festibook.festibook_backend.event.controller;

import com.festibook.festibook_backend.core.share.Region;
import com.festibook.festibook_backend.event.entity.Event;
import com.festibook.festibook_backend.event.useCase.ReadEventSearchUseCase;
import com.festibook.festibook_backend.event.useCase.ReadEventSearchUseCase.ReadEventSearchRequest;
import com.festibook.festibook_backend.event.useCase.ReadEventSearchUseCase.ReadEventSearchResponse;
import com.festibook.festibook_backend.event.useCase.ReadEventUseCase;
import com.festibook.festibook_backend.event.useCase.ReadEventUseCase.ReadEventRequest;
import com.festibook.festibook_backend.event.useCase.ReadEventUseCase.ReadEventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReadEventController {

  private final ReadEventUseCase readEventUseCase;

  private final ReadEventSearchUseCase readEventSearchUseCase;

  @Operation(
      summary = "이벤트 조회 API (지역 필터)",
      description = "지역을 기준으로 이벤트를 조회합니다. \n\n region의 값이 All인 경우 모든 이벤트를 조회합니다.",
      operationId = "/events"
  )
  @GetMapping("/events")
  public ResponseEntity<List<Event>> readEventDetail(
      @ParameterObject ReadAdminHotelDetailRequest request) {
    ReadEventResponse readEventResponse = readEventUseCase.execute(ReadEventRequest.builder()
        .region(request.getRegion())
        .build());
    return ResponseEntity.ok(readEventResponse.getEvents());
  }

  @Operation(
      summary = "이벤트 조회 API (제목, 지역 검색)",
      description = "keyword 기준으로 이벤트를 조회합니다. \n\n Category의 값이 All인 경우 제목과 지역 중 하나라도 존재하는 경우 조회합니다.",
      operationId = "/events/search"
  )
  @GetMapping("/events/search")
  public ResponseEntity<List<Event>> readEventDetailSearch(
      @ParameterObject ReadAdminHotelDetailSearchRequest request) {
    ReadEventSearchResponse readEventSearchResponse = readEventSearchUseCase.execute(
        ReadEventSearchRequest.builder()
            .keyword(request.getKeyword())
            .category(request.getCategory())
            .build());
    return ResponseEntity.ok(readEventSearchResponse.getEvents());
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  @Schema(description = "이벤트 조회 요청")
  public static class ReadAdminHotelDetailRequest {

    @Schema(description = "필터 지역", example = "SEOUL", allowableValues = {"ALL", "SEOUL", "INCHEON",
        "DAEJEON", "DAEGU", "GWANGJU", "BUSAN", "ULSAN", "GYEONGGI", "GANGWON", "CHUNGNAM",
        "CHUNGBUK", "JEONBUK", "SEJONG", "GYEONGNAM", "JEONNAM", "GYEONGBUK", "JEJU"})
    private Region region;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  @Schema(description = "이벤트 조회 요청")
  public static class ReadAdminHotelDetailSearchRequest {

    private String keyword;

    private Category category;

    public enum Category {
      All, REGION, TITLE
    }
  }
}

