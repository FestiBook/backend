package com.festibook.festibook_backend.event.repository;

import com.festibook.festibook_backend.event.entity.Event;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
