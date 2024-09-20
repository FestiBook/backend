package com.festibook.festibook_backend.event.repository;

import com.festibook.festibook_backend.event.entity.PopularitySearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularitySearchKeywordRepository extends
    JpaRepository<PopularitySearchKeyword, Long> {
  

  PopularitySearchKeyword findByKeyword(String keyword);
}
