package com.festibook.festibook_backend.review.service;

import com.festibook.festibook_backend.review.dto.ReviewRequestDto;
import com.festibook.festibook_backend.review.dto.ReviewResponseDto;
import com.festibook.festibook_backend.review.entity.Review;
import com.festibook.festibook_backend.review.repository.ReviewRepository;
import com.festibook.festibook_backend.user.entity.User;
import com.festibook.festibook_backend.user.repository.UserRepository;
import com.festibook.festibook_backend.event.entity.Event;
import com.festibook.festibook_backend.event.repository.EventRepository;
import com.festibook.festibook_backend.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TokenService tokenService;

    public List<ReviewResponseDto> getReviewsByEvent(Long eventId) {
        List<Review> reviews = reviewRepository.findByEventId(eventId);
        return reviews.stream()
                .map(ReviewResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ReviewResponseDto createReview(Long eventId, ReviewRequestDto requestDto, HttpServletRequest request) {
        tokenService.validateAccessToken(request);
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Review review = Review.builder()
                .user(user)
                .event(event)
                .content(requestDto.getContent())
                .build();

        Review savedReview = reviewRepository.save(review);
        return ReviewResponseDto.fromEntity(savedReview);
    }

    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, HttpServletRequest request) {
        tokenService.validateAccessToken(request);
        Long userId = tokenService.getUserIdFromToken(request);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        review.setContent(requestDto.getContent());
        Review updatedReview = reviewRepository.save(review);
        return ReviewResponseDto.fromEntity(updatedReview);
    }

    public void deleteReview(Long reviewId, HttpServletRequest request) {
        tokenService.validateAccessToken(request);
        Long userId = tokenService.getUserIdFromToken(request);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        reviewRepository.delete(review);
    }

    public List<ReviewResponseDto> getReviewsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Review> reviews = reviewRepository.findByUser(user);
        return reviews.stream()
                .map(ReviewResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getMyReviews(HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Review> reviews = reviewRepository.findByUser(user);
        return reviews.stream()
                .map(ReviewResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
