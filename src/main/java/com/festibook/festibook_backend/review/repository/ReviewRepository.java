package com.festibook.festibook_backend.review.repository;

import com.festibook.festibook_backend.review.entity.Review;
import com.festibook.festibook_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEventId(Long eventId);
    List<Review> findByUser(User user);
}
