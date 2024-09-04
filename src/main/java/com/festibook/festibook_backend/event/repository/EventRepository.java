package com.festibook.festibook_backend.event.repository;

import com.festibook.festibook_backend.event.entity.Event;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
  Optional<Event> findByContentId(int contentId);
}
