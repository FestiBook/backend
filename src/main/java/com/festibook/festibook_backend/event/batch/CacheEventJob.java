package com.festibook.festibook_backend.event.batch;


import com.festibook.festibook_backend.event.entity.Event;
import com.festibook.festibook_backend.event.repository.EventRepository;
import com.festibook.festibook_backend.event.useCase.CacheEventUseCase;
import com.festibook.festibook_backend.event.useCase.CacheEventUseCase.CacheEventRequest;
import com.festibook.festibook_backend.event.useCase.CacheEventUseCase.CacheEventResponse;
import com.festibook.festibook_backend.external.event.dto.EventResponse.Item;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class CacheEventJob {

  private final CacheEventUseCase cacheEventUseCase;

  private final EventRepository eventRepository;

  @PostConstruct
  @Scheduled(cron = "0 0 1 * * *")
  public void CacheEventJobScheduler() {
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String formattedDate = currentDate.format(formatter);

    CacheEventResponse cacheEventResponse = cacheEventUseCase.execute(CacheEventRequest.builder()
        .eventStartDate(formattedDate)
        .build());

    List<Item> items = cacheEventResponse.getItems();
    List<Event> events = new ArrayList<>();
    for (Item item : items) {
      Optional<Event> existingEvent = eventRepository.findByContentId(item.getContentid());
      if (existingEvent.isPresent()) {
        Event eventToUpdate = existingEvent.get();
        updateEvent(eventToUpdate, item);
        events.add(eventToUpdate);
      } else {
        events.add(createEvent(item));
      }
    }

    eventRepository.saveAll(events);
  }

  private void updateEvent(Event event, Item item) {
    event.setTitle(item.getTitle());
    event.setAddress1(item.getAddr1());
    event.setAddress2(item.getAddr2());
    event.setOriginUrl(item.getFirstimage());
    event.setThumbnailUrl(item.getFirstimage2());
    event.setStartDate(item.getEventstartdate());
    event.setEndDate(item.getEventenddate());
    event.setMapX(item.getMapx());
    event.setMapY(item.getMapy());
  }

  private Event createEvent(Item item) {
    return Event.builder()
        .contentId(item.getContentid())
        .title(item.getTitle())
        .address1(item.getAddr1())
        .address2(item.getAddr2())
        .originUrl(item.getFirstimage())
        .thumbnailUrl(item.getFirstimage2())
        .startDate(item.getEventstartdate())
        .endDate(item.getEventenddate())
        .mapX(item.getMapx())
        .mapY(item.getMapy())
        .build();
  }
}
