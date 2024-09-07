package com.festibook.festibook_backend.favorite.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteEventResponseDto {
    private Long eventId;
    private String title;
    private String address1;
    private String startDate;
    private String endDate;
    private String thumbnailUrl;
}
