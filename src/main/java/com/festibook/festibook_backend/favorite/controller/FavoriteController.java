package com.festibook.festibook_backend.favorite.controller;

import com.festibook.festibook_backend.favorite.dto.FavoriteEventResponseDto;
import com.festibook.festibook_backend.favorite.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.List;

@RestController
@RequestMapping("/api/favorite")
@Tag(name = "Favorites", description = "즐겨찾기 관련 API")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    @Operation(summary = "즐겨찾기 추가", description = "이벤트를 즐겨찾기에 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "즐겨찾기 추가 성공",
                    content = @Content(schema = @Schema(implementation = FavoriteEventResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 이미 즐겨찾기 목록에 있는 경우"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<FavoriteEventResponseDto> addFavorite(@RequestParam Long eventId, HttpServletRequest request) {
        FavoriteEventResponseDto favoriteEvent = favoriteService.addFavorite(eventId, request);
        return new ResponseEntity<>(favoriteEvent, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "즐겨찾기 목록 조회", description = "사용자의 즐겨찾기 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "즐겨찾기 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = FavoriteEventResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<List<FavoriteEventResponseDto>> getFavoriteList(HttpServletRequest request) {
        List<FavoriteEventResponseDto> favoriteList = favoriteService.getFavoriteList(request);
        return new ResponseEntity<>(favoriteList, HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "즐겨찾기 삭제", description = "즐겨찾기에서 이벤트를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "즐겨찾기 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 즐겨찾기에 없는 이벤트인 경우"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<Void> removeFavorite(@RequestParam Long eventId, HttpServletRequest request) {
        favoriteService.removeFavorite(eventId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
