package com.festibook.festibook_backend.external.event.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class EventDetailIntroResponse {

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

    private String contentid;
    private String contenttypeid;
    private String sponsor1;
    private String sponsor1tel;
    private String sponsor2;
    private String sponsor2tel;
    private String eventenddate;
    private String playtime;
    private String eventplace;
    private String eventhomepage;
    private String agelimit;
    private String bookingplace;
    private String placeinfo;
    private String subevent;
    private String program;
    private String eventstartdate;
    private String usetimefestival;
    private String discountinfofestival;
    private String spendtimefestival;
    private String festivalgrade;
  }

}
