package com.festibook.festibook_backend.review.dto;

import com.festibook.festibook_backend.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long id;
    private Long userId;
    private String nickname;
    private String content;
    private Long eventId;
    private String eventName;
    private LocalDateTime createdAt;

    public static ReviewResponseDto fromEntity(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.id = review.getId();
        dto.userId = review.getUser().getId();
        dto.nickname = review.getUser().getNickname();
        dto.content = review.getContent();
        dto.eventId = review.getEvent().getId();
        dto.eventName = review.getEvent().getTitle();
        dto.createdAt = review.getCreatedAt();
        return dto;
    }
}
