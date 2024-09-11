package com.festibook.festibook_backend.core.share;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Region {

  ALL(""),
  SEOUL("서울특별시"),
  INCHEON("인천광역시"),
  DAEJEON("대전광역시"),
  DAEGU("대구광역시"),
  GWANGJU("광주광역시"),
  BUSAN("부산광역시"),
  ULSAN("울산광역시"),
  GYEONGGI("경기도"),
  GANGWON("강원특별자치도"),
  CHUNGNAM("충청남도"),
  CHUNGBUK("충청북도"),
  JEONBUK("전북특별자치도"),
  SEJONG("세종특별자치시"),
  GYEONGNAM("경상남도"),
  JEONNAM("전라남도"),
  GYEONGBUK("경상북도"),
  JEJU("제주특별자치도");

  private final String description;
}

