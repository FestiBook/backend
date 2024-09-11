package com.festibook.festibook_backend.event.useCase;

import com.festibook.festibook_backend.event.controller.ReadEventController.ReadAdminHotelDetailSearchRequest.Category;
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
public class ReadEventSearchUseCase {

  private final EventRepository eventRepository;

  public ReadEventSearchResponse execute(ReadEventSearchRequest request) {
    List<Event> events = null;
    if (request.getCategory() == null || Category.All.equals(request.getCategory())) {
      events = eventRepository.findByTitleOrAddress(request.getKeyword());
    }
    if (Category.TITLE.equals(request.getCategory())) {
      events = eventRepository.findByTitle(request.getKeyword());
    }
    if (Category.REGION.equals(request.getCategory())) {
      events = eventRepository.findByAddress(request.getKeyword());
    }
    return ReadEventSearchResponse.builder()
        .events(events)
        .build();
  }


  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadEventSearchResponse {

    private List<Event> events;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadEventSearchRequest {

    private String keyword;

    private Category category;
  }

}
