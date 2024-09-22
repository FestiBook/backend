package com.festibook.festibook_backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.openai.api.OpenAiApi;

@Configuration
public class OpenAiConfig {

    @Value("${spring.ai.openai.chat.api-key}")
    private String GPT_API_KEY;

    @Bean
    public OpenAiApi openAiApi() {
        // OpenAiApi 인스턴스를 생성할 때 필요한 API 키를 전달
        String apiKey = GPT_API_KEY;
        return new OpenAiApi(apiKey);
    }
}
