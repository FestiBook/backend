package com.festibook.festibook_backend.core.util;

import com.festibook.festibook_backend.configuration.ApplicationContextProvider;
import com.festibook.festibook_backend.core.exception.OkhttpException;
import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.HttpMethod;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class OkhttpRequest {

  private static final OkHttpClient client = ApplicationContextProvider.getBean("okHttpClient",
      OkHttpClient.class);

  protected final OkhttpMediaType mediaType;

  protected final String url;

  protected final Object request;

  protected final HttpMethod httpMethod;


  public abstract String convertRequestToString();

  private MediaType getMediaType() {
    return MediaType.parse(this.mediaType.getType());
  }

  public Response request() {
    Response response = null;
    StringBuilder logStringBuilder = new StringBuilder();
    RequestBody requestBody = RequestBody.create(convertRequestToString(), getMediaType());
    Request.Builder requestBuilder = new Request.Builder().url(this.url);

    if (this.httpMethod == HttpMethod.GET) {
      requestBuilder.method(this.httpMethod.name(), null);
    } else {
      requestBuilder.method(this.httpMethod.name(), requestBody);
    }
    Request request = requestBuilder.build();
    try {
      response = client.newCall(request)
          .execute();

      logStringBuilder.append("Request Url : ");
      logStringBuilder.append(this.url);
      log.info(logStringBuilder.toString());
    } catch (IOException e) {
      logStringBuilder.append("[Okhttp Request Fail]");
      logStringBuilder.append("Request Url : ");
      logStringBuilder.append(this.url);
      log.error(logStringBuilder.toString());
      throw new OkhttpException(e.getMessage());
    }
    ResponseBody responseBody = response.body();
    if (responseBody == null || !response.isSuccessful()) {
      logStringBuilder.append("\n");
      logStringBuilder.append("[Okhttp Response Fail]");
      if (responseBody != null) {
        try {
          logStringBuilder.append("Response Body : ");
          logStringBuilder.append(response.peekBody(Long.MAX_VALUE)
              .string());
        } catch (IOException e) {
          log.error("[OkhttpRequest] Read Response Body Has Error ", e);
        }
      }
      log.error(logStringBuilder.toString());
      return response;
    }
    return response;
  }

  @RequiredArgsConstructor
  @Getter
  protected enum OkhttpMediaType {
    XML("text/xml; charset=utf-8"), JSON("application/json; charset=utf-8");

    private final String type;
  }
}


