package com.festibook.festibook_backend.user.controller;

import com.festibook.festibook_backend.user.dto.UserRequestDto;
import com.festibook.festibook_backend.user.dto.UserResponseDto;
import com.festibook.festibook_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "유저 관련 API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "유저 정보 조회", description = "현재 로그인된 유저의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<UserResponseDto> getUserInfo(HttpServletRequest request) {
        UserResponseDto user = userService.getUserInfo(request);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping
    @Operation(summary = "유저 닉네임 수정", description = "유저 닉네임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(description = "수정할 유저 정보", required = true) @RequestBody UserRequestDto requestDto,
            @Parameter(description = "액세스 토큰", required = true) HttpServletRequest request) {
        UserResponseDto updatedUser = userService.updateUser(requestDto, request);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "유저 탈퇴", description = "현재 로그인된 유저를 탈퇴시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "액세스 토큰", required = true) HttpServletRequest request) {
        userService.deleteUser(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "현재 로그인된 유저를 로그아웃시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<Void> logoutUser(
            @Parameter(description = "액세스 토큰", required = true) HttpServletRequest request) {
        userService.logout(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
