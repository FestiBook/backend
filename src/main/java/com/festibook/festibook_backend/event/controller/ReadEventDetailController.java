package com.festibook.festibook_backend.event.controller;

import com.festibook.festibook_backend.event.controller.ReadEventDetailController.ReadEventDetailResponse.Event;
import com.festibook.festibook_backend.event.controller.ReadEventDetailController.ReadEventDetailResponse.Event.Connect;
import com.festibook.festibook_backend.event.controller.ReadEventDetailController.ReadEventDetailResponse.Event.Image;
import com.festibook.festibook_backend.event.controller.ReadEventDetailController.ReadEventDetailResponse.Event.Location;
import com.festibook.festibook_backend.event.useCase.ReadEventDetailUseCase;
import com.festibook.festibook_backend.event.useCase.ReadEventDetailUseCase.ReadEventDetailRequest;
import com.festibook.festibook_backend.external.event.dto.EventDetailCommonResponse;
import com.festibook.festibook_backend.external.event.dto.EventDetailIntroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReadEventDetailController {

  private final ReadEventDetailUseCase readEventDetailUseCase;

  @Operation(
      summary = "이벤트 상세 조회 API",
      description = "이벤트를 상세 조회합니다.",
      operationId = "/events/{eventId}"
  )
  @GetMapping("/events/{eventId}")
  public ResponseEntity<ReadEventDetailResponse> readEventDetail(
      @PathVariable(name = "eventId") @Schema(example = "1234") Integer eventId) {
    ReadEventDetailUseCase.ReadEventDetailResponse readEventDetailResponse = readEventDetailUseCase.execute(
        ReadEventDetailRequest.builder()
            .eventId(Optional.ofNullable(eventId).orElse(0))
            .build());
    return ResponseEntity.ok(toResponse(readEventDetailResponse));
  }

  private ReadEventDetailResponse toResponse(
      ReadEventDetailUseCase.ReadEventDetailResponse readEventDetailResponse) {
    com.festibook.festibook_backend.event.entity.Event event = readEventDetailResponse.getEvent();
    EventDetailCommonResponse.Item commonItem = readEventDetailResponse.getEventDetailCommonResponse()
        .getResponse().getBody().getItems().getItem().get(0);
    EventDetailIntroResponse.Item introItem = readEventDetailResponse.getEventDetailIntroResponse()
        .getResponse().getBody().getItems().getItem().get(0);
    return ReadEventDetailResponse.builder()
        .event(new Event(
            event.getTitle(),
            commonItem.getOverview(),
            event.getStartDate(),
            event.getEndDate(),
            null,
            introItem.getPlaytime(),
            new Image(event.getOriginUrl(),
                event.getThumbnailUrl()),
            new Location(event.getAddress1(),
                event.getAddress2(),
                event.getMapX(),
                event.getMapY()),
            introItem.getUsetimefestival(),
            new Connect(
                introItem.getSponsor1tel(), introItem.getSponsor2tel(),
                introItem.getEventhomepage())
        ))
        .build();
  }

  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  @Schema(description = "이벤트 상세 응답")
  public static class ReadEventDetailResponse {

    private Event event;

    public record Event(
        @Schema(description = "이벤트 제목", example = "가야문화축제") String title,
        @Schema(description = "이벤트 설명", example = "김해시에서 개최되는 가야문화축제입니다.") String description,
        @Schema(description = "시작 날짜", example = "2024-10-16") String startDate,
        @Schema(description = "종료 날짜", example = "2024-10-20") String endDate,
        @Schema(description = "이벤트 좋아요 여부", example = "true") Boolean isHearted,
        @Schema(description = "이벤트 시간", example = "10:00") String playtime,
        @Schema(description = "이벤트 이미지 정보") Image image,
        @Schema(description = "이벤트 장소 정보") Location location,
        @Schema(description = "가격 설명", example = "무료 입장") String price,
        @Schema(description = "이벤트 연락처 정보") Connect connect
    ) {

      public record Image(
          @Schema(description = "원본 이미지 URL", example = "https://example.com/image.jpg") String originUrl,
          @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.jpg") String thumbnailUrl
      ) {

      }

      public record Location(
          @Schema(description = "행정 구역", example = "경상남도 김해시") String administrativeArea,
          @Schema(description = "상세 주소", example = "김해시 수릉원") String detailedAddress,
          @Schema(description = "경도", example = "128.8894") double longitude,
          @Schema(description = "위도", example = "35.2345") double latitude
      ) {

      }

      public record Connect(
          @Schema(description = "주최자 전화번호", example = "055-330-6840") String organizerPhone,
          @Schema(description = "주관사 전화번호", example = "055-330-6840") String hostPhone,
          @Schema(description = "웹사이트 URL", example = "https://example.com") String websiteUrl
      ) {

      }
    }
  }

}
