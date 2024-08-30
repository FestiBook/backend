package com.festibook.festibook_backend.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.http.HttpMethod;

public class OkhttpJsonRequest extends OkhttpRequest {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public OkhttpJsonRequest(String url, Object request, HttpMethod httpMethod) {
    super(OkhttpMediaType.JSON, url, request, httpMethod);
  }

  @Override
  public String convertRequestToString() {
    try {
      return objectMapper.writeValueAsString(request);
    } catch (JsonProcessingException e) {
      return null;
    }
  }
}

