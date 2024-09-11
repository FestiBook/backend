package com.festibook.festibook_backend.event.repository;

import com.festibook.festibook_backend.event.entity.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {

  Optional<Event> findByContentId(int contentId);

  @Query("SELECT e FROM Event e WHERE e.address1 LIKE %:address%")
  List<Event> findByAddress(String address);

  @Query("SELECT e FROM Event e WHERE e.title LIKE %:title%")
  List<Event> findByTitle(String title);

  @Query("SELECT e FROM Event e WHERE (e.title LIKE %:keyword% OR e.address1 LIKE %:keyword%)")
  List<Event> findByTitleOrAddress(String keyword);
}
