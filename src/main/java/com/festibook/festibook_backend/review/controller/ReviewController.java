package com.festibook.festibook_backend.review.controller;

import com.festibook.festibook_backend.review.dto.ReviewRequestDto;
import com.festibook.festibook_backend.review.dto.ReviewResponseDto;
import com.festibook.festibook_backend.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "리뷰 관련 API")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("events/{eventId}")
    @Operation(summary = "이벤트별 리뷰 조회", description = "특정 이벤트에 대한 모든 리뷰를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 목록이 성공적으로 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음.", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.", content = @Content)
    })
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByEvent(
            @Parameter(description = "조회할 이벤트 ID", required = true) @PathVariable Long eventId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByEvent(eventId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{eventId}")
    @Operation(summary = "리뷰 작성", description = "특정 이벤트에 대한 리뷰를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 작성되었습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청.", content = @Content),
            @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음.", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.", content = @Content)
    })
    public ResponseEntity<ReviewResponseDto> createReview(
            @Parameter(description = "이벤트 ID", required = true) @PathVariable Long eventId,
            @Parameter(description = "리뷰 내용", required = true) @RequestBody ReviewRequestDto requestDto,
            @Parameter(description = "액세스 토큰", required = true) HttpServletRequest request) {
        ReviewResponseDto review = reviewService.createReview(eventId, requestDto, request);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 수정되었습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음.", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.", content = @Content)
    })
    public ResponseEntity<ReviewResponseDto> updateReview(
            @Parameter(description = "수정할 리뷰 ID", required = true) @PathVariable Long reviewId,
            @Parameter(description = "리뷰 수정 내용", required = true) @RequestBody ReviewRequestDto requestDto,
            @Parameter(description = "액세스 토큰", required = true) HttpServletRequest request) {
        ReviewResponseDto review = reviewService.updateReview(reviewId, requestDto, request);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "특정 리뷰를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음.", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.", content = @Content)
    })
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "삭제할 리뷰 ID", required = true) @PathVariable Long reviewId,
            @Parameter(description = "액세스 토큰", required = true) HttpServletRequest request) {
        reviewService.deleteReview(reviewId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("users/{userId}")
    @Operation(summary = "유저별 리뷰 조회", description = "특정 유저가 작성한 모든 리뷰를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저의 리뷰 목록이 성공적으로 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음.", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.", content = @Content)
    })
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUser(
            @Parameter(description = "조회할 유저 ID", required = true) @PathVariable Long userId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping
    @Operation(summary = "자신의 리뷰 조회", description = "현재 로그인한 유저가 작성한 모든 리뷰를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 목록이 성공적으로 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패.", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.", content = @Content)
    })
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(HttpServletRequest request) {
        List<ReviewResponseDto> reviews = reviewService.getMyReviews(request);
        return ResponseEntity.ok(reviews);
    }
}
