package com.festibook.festibook_backend.external.event.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

  private int numOfRows;

  private int pageNo;

  private String MobileOS;

  private String MobileApp;

  private String _type;

  private String listYN;

  private String arrange;

  private String eventStartDate;

  private String serviceKey;
}