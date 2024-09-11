package com.festibook.festibook_backend.event.useCase;

import com.festibook.festibook_backend.core.share.Region;
import com.festibook.festibook_backend.event.entity.Event;
import com.festibook.festibook_backend.event.repository.EventRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadEventUseCase {

  private final EventRepository eventRepository;

  public ReadEventResponse execute(ReadEventRequest request) {
    List<Event> events = eventRepository.findByAddress(request.getRegion().getDescription());
    return ReadEventResponse.builder()
        .events(events)
        .build();
  }


  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadEventResponse {

    private List<Event> events;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadEventRequest {

    private Region region;
  }

}
