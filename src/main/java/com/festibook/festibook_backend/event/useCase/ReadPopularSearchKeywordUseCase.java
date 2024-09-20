package com.festibook.festibook_backend.event.useCase;

import com.festibook.festibook_backend.event.entity.PopularitySearchKeyword;
import com.festibook.festibook_backend.event.repository.PopularitySearchKeywordRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadPopularSearchKeywordUseCase {

  private final PopularitySearchKeywordRepository popularitySearchKeywordRepository;

  public ReadPopularSearchKeywordResponse execute() {
    List<PopularitySearchKeyword> popularitySearchKeywords = popularitySearchKeywordRepository.findAll(
        Sort.by(
            Direction.DESC, "count"));
    return ReadPopularSearchKeywordResponse.builder()
        .popularitySearchKeywords(popularitySearchKeywords)
        .build();
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadPopularSearchKeywordResponse {

    private List<PopularitySearchKeyword> popularitySearchKeywords;
  }
}
