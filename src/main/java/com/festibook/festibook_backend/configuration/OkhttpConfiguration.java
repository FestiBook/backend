package com.festibook.festibook_backend.configuration;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkhttpConfiguration {

  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder().build();
  }
}