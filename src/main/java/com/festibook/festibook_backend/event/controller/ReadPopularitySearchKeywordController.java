package com.festibook.festibook_backend.event.controller;

import com.festibook.festibook_backend.event.controller.ReadPopularitySearchKeywordController.ReadPopularitySearchKeywordResponse.PopularitySearchKeyword;
import com.festibook.festibook_backend.event.useCase.ReadPopularSearchKeywordUseCase;
import com.festibook.festibook_backend.event.useCase.ReadPopularSearchKeywordUseCase.ReadPopularSearchKeywordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReadPopularitySearchKeywordController {

  private final ReadPopularSearchKeywordUseCase readPopularSearchKeywordUseCase;

  @Operation(
      summary = "인기 검색어 조회 API",
      description = "인기 검색어를 조회합니다.",
      operationId = "/keyword/popularity"
  )
  @GetMapping("/keyword/popularity")
  public ResponseEntity<ReadPopularitySearchKeywordResponse> readPopularitySearchKeyword() {
    ReadPopularSearchKeywordResponse readPopularSearchKeywordResponse = readPopularSearchKeywordUseCase.execute();
    return ResponseEntity.ok(toResponse(readPopularSearchKeywordResponse));
  }

  private ReadPopularitySearchKeywordResponse toResponse(
      ReadPopularSearchKeywordResponse readPopularSearchKeywordResponse) {
    List<PopularitySearchKeyword> popularitySearchKeywords = readPopularSearchKeywordResponse.getPopularitySearchKeywords()
        .stream()
        .map(keyword -> new PopularitySearchKeyword(
            keyword.getKeyword(),
            keyword.getCount()
        ))
        .toList();
    return ReadPopularitySearchKeywordResponse.builder()
        .popularityKeywords(popularitySearchKeywords)
        .build();
  }

  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  @Schema(description = "인기 검색어 응답")
  public static class ReadPopularitySearchKeywordResponse {

    private List<PopularitySearchKeyword> popularityKeywords;

    public record PopularitySearchKeyword(
        @Schema(description = "검색어", example = "가야문화축제") String keyword,
        @Schema(description = "검색 횟수", example = "5") Integer count
    ) {

    }

  }
}
