package com.festibook.festibook_backend.external.event.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class EventDetailCommonResponse {

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
    private String title;
    private String createdtime;
    private String modifiedtime;
    private String tel;
    private String telname;
    private String homepage;
    private String booktour;
    private String overview;
  }
}
