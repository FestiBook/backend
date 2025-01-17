package com.festibook.festibook_backend.event.entity;

import com.festibook.festibook_backend.core.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Event extends BaseEntity {

    private int contentId;

    private String title;

    private String address1;

    private String address2;

    private String originUrl;

    private String startDate;

    private String endDate;

    private String thumbnailUrl;

    private double mapX;

    private double mapY;

    @Builder.Default
    private int favoriteCount = 0;
}
