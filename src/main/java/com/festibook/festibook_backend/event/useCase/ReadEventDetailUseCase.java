package com.festibook.festibook_backend.event.useCase;

import com.festibook.festibook_backend.event.entity.Event;
import com.festibook.festibook_backend.event.repository.EventRepository;
import com.festibook.festibook_backend.event.useCase.ReadEventDetailCommonUseCase.ReadEventDetailCommonRequest;
import com.festibook.festibook_backend.event.useCase.ReadEventDetailCommonUseCase.ReadEventDetailCommonResponse;
import com.festibook.festibook_backend.event.useCase.ReadEventDetailIntroUseCase.ReadEventDetailIntroRequest;
import com.festibook.festibook_backend.event.useCase.ReadEventDetailIntroUseCase.ReadEventDetailIntroResponse;
import com.festibook.festibook_backend.external.event.dto.EventDetailCommonResponse;
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
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReadEventDetailUseCase {

  private final ReadEventDetailCommonUseCase readEventDetailCommonUseCase;

  private final ReadEventDetailIntroUseCase readEventDetailIntroUseCase;

  private final EventRepository eventRepository;

  public ReadEventDetailResponse execute(ReadEventDetailRequest request) {
    ReadEventDetailCommonResponse readEventDetailCommonResponse = readEventDetailCommonUseCase.execute(
        ReadEventDetailCommonRequest.builder()
            .eventId(request.getEventId())
            .build());
    ReadEventDetailIntroResponse readEventDetailIntroResponse = readEventDetailIntroUseCase.execute(
        ReadEventDetailIntroRequest.builder()
            .eventId(request.getEventId())
            .build());
    Event event = eventRepository.findByContentId(request.getEventId()).orElse(null);
    return ReadEventDetailResponse.builder()
        .event(event)
        .eventDetailCommonResponse(readEventDetailCommonResponse.getEventDetailCommonResponse())
        .eventDetailIntroResponse(readEventDetailIntroResponse.getEventDetailIntroResponse())
        .build();
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadEventDetailResponse {

    private Event event;

    private EventDetailCommonResponse eventDetailCommonResponse;

    private EventDetailIntroResponse eventDetailIntroResponse;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadEventDetailRequest {

    private Integer eventId;
  }

}

