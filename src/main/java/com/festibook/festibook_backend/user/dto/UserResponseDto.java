package com.festibook.festibook_backend.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String nickname;
    private String platform;
}
