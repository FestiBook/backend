package com.festibook.festibook_backend.event.useCase;

import com.festibook.festibook_backend.event.controller.ReadEventController.ReadAdminHotelDetailSearchRequest.Category;
import com.festibook.festibook_backend.event.entity.Event;
import com.festibook.festibook_backend.event.entity.PopularitySearchKeyword;
import com.festibook.festibook_backend.event.repository.EventRepository;
import com.festibook.festibook_backend.event.repository.PopularitySearchKeywordRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class ReadEventSearchUseCase {

  private final EventRepository eventRepository;

  private final PopularitySearchKeywordRepository popularitySearchKeywordRepository;

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
    if (events != null && !events.isEmpty()) {
      PopularitySearchKeyword popularitySearchKeyword = popularitySearchKeywordRepository.findByKeyword(
          request.getKeyword());
      if (popularitySearchKeyword == null) {
        PopularitySearchKeyword newPopularitySearchKeyword = PopularitySearchKeyword.builder()
            .keyword(request.getKeyword())
            .count(0)
            .build();
        popularitySearchKeywordRepository.save(newPopularitySearchKeyword);
        popularitySearchKeyword = newPopularitySearchKeyword;
      }
      popularitySearchKeyword.setCount(popularitySearchKeyword.getCount() + 1);
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
