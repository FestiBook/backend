package com.festibook.festibook_backend.external.event.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class EventResponse {
  private Response response;

  @ToString
  @Getter
  @Setter
  public static class Response {
    private Header header;
    private Body body;
  }

  @ToString
  @Getter
  @Setter
  public static class Header {
    private String resultCode;
    private String resultMsg;
  }

  @ToString
  @Getter
  @Setter
  public static class Body {
    private Items items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
  }

  @ToString
  @Getter
  @Setter
  public static class Items {
    private List<Item> item;
  }

  @ToString
  @Getter
  @Setter
  public static class Item {
    private String addr1;
    private String addr2;
    private String booktour;
    private String cat1;
    private String cat2;
    private String cat3;
    private Integer contentid;
    private Integer contenttypeid;
    private String createdtime;
    private String eventstartdate;
    private String eventenddate;
    private String firstimage;
    private String firstimage2;
    private String cpyrhtDivCd;
    private Double mapx;
    private Double mapy;
    private Integer mlevel;
    private String modifiedtime;
    private String areacode;
    private String sigungucode;
    private String tel;
    private String title;
  }
}

