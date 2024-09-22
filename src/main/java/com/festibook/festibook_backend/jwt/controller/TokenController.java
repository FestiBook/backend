package com.festibook.festibook_backend.jwt.controller;

import com.festibook.festibook_backend.jwt.dto.TokenRequestDto;
import com.festibook.festibook_backend.jwt.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/token")
@Tag(name = "Token", description = "토큰 관련 API")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    @Operation(summary = "액세스 토큰 재발급", description = "리프레쉬 토큰을 사용해 새로운 액세스 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "액세스 토큰 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "리프레쉬 토큰이 만료되었습니다."),
            @ApiResponse(responseCode = "403", description = "유효하지 않은 리프레쉬 토큰입니다.")
    })
    public ResponseEntity<String> refreshAccessToken(@RequestBody TokenRequestDto tokenRequest) {
        String newAccessToken = tokenService.reGenerateAccessToken(tokenRequest.getRefreshToken());
        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }
}
