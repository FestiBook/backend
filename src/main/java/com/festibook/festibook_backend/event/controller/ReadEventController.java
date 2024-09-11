package com.festibook.festibook_backend.event.controller;

import com.festibook.festibook_backend.core.share.Region;
import com.festibook.festibook_backend.event.entity.Event;
import com.festibook.festibook_backend.event.useCase.ReadEventUseCase;
import com.festibook.festibook_backend.event.useCase.ReadEventUseCase.ReadEventRequest;
import com.festibook.festibook_backend.event.useCase.ReadEventUseCase.ReadEventResponse;
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

  @GetMapping("/events")
  public ResponseEntity<List<Event>> readEventDetail(
      @ParameterObject ReadAdminHotelDetailRequest request) {
    ReadEventResponse readEventResponse = readEventUseCase.execute(ReadEventRequest.builder()
        .region(request.getRegion())
        .build());
    return ResponseEntity.ok(readEventResponse.getEvents());
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
}

