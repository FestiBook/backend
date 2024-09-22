package com.festibook.festibook_backend.event.itineraries;

import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletion;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class ItineraryService {

    private final OpenAiApi openAiApi;

    public ItineraryService(OpenAiApi openAiApi) {
        this.openAiApi = openAiApi;
    }

    public String askQuestion(String departureLocation, String arrivalLocation, String departureDate, String arrivalDate,
                              int numberOfPeople, String travelIntensity, int totalBudget, int mealCount) {

        String dynamicPrompt = String.format("""
            Festibook은 사용자가 입력하는 여행 정보를 바탕으로 맞춤형 여행 일정을 제공합니다. 사용자가 입력한 정보를 분석하여 최적의 여행 경로를 추천하세요. 요청에 따라 일정을 맞춤으로 제공해야 합니다.

            요청 정보는 다음과 같습니다:

            - 출발위치 (departureLocation): %s
            - 도착위치 (arrivalLocation): %s
            - 여행 출발날짜 (departureDate): %s
            - 여행 끝나는날짜 (arrivalDate): %s
            - 인원수 (numberOfPeople): %d명
            - 여행 강도 (travelIntensity): %s
            - 총 예산 (totalBudget): %d만원
            - 식사 횟수 (mealCount): %d회

            위 정보를 바탕으로 여행 경로를 추천할 때는 다음 사항을 준수하세요:

            - 답변은 JSON 형식으로만 응답하여 주세요.
            - 추천하는 여행 경로에 **정확한 장소명과 주소**를 포함해 주세요. 장소는 반드시 존재하는 실제 장소여야 하고, Google 지도에 확인된 장소만 추천해야 합니다.
            - 응답은 {"itinerary": [일정 Array]} 형식으로 JSON 배열을 제공해야 합니다.
            - 일정 JSON 외에는 다른 내용이 절대 포함되지 않아야 합니다.
            - JSON 형식은 아래와 같이 맞춰야 합니다:
            {
              'itinerary': [
                {
                  'date': 'MM-DD',
                  'schedule': [
                    {
                      'time': 'HH:MM',
                      'duration': 'XX분',
                      'location': {
                        'name': '정확한 장소명',
                        'administrativeArea': '행정구역명',
                        'detailedAddress': '세부 주소',
                        'longitude': '경도',
                        'latitude': '위도'
                      },
                      'price': {
                        'currency': 'KRW',
                        'price': 'XX만원'
                      },
                      'description': '내용 설명'
                    }
                  ]
                }
              ]
            }

            - 날짜는 MM-DD 형식, 시간은 24시간 형식이어야 합니다.
            - 비용은 만원 단위로 계산해서 'XX만원'으로 작성하고, 1인당 비용을 포함하며 숙박비, 교통비를 포함합니다.
            - 무료인 경우에도 비용은 '0만원'으로 입력해 주세요.
            - 모든 일정은 하루에 최소 3개 이상 포함되어야 하고, 기본 식사 횟수는 3회로 설정됩니다.
            - 여행 일정 외의 질문에는 응답하지 마세요. 거절의 말도 JSON 일정 형식으로 제공하세요.
            """, departureLocation, arrivalLocation, departureDate, arrivalDate, numberOfPeople, travelIntensity, totalBudget, mealCount);

        ChatCompletionMessage systemMessage = new ChatCompletionMessage(dynamicPrompt, ChatCompletionMessage.Role.SYSTEM);
        ChatCompletionMessage userMessage = new ChatCompletionMessage("여행 일정을 추천해줘.", ChatCompletionMessage.Role.USER);

        ChatCompletionRequest request = new ChatCompletionRequest(
                List.of(systemMessage, userMessage),
                OpenAiApi.ChatModel.GPT_4_O_MINI.getValue(),
                0.7
        );

        ResponseEntity<ChatCompletion> responseEntity = openAiApi.chatCompletionEntity(request);
        ChatCompletion completion = responseEntity.getBody();

        if (completion != null && !completion.choices().isEmpty()) {
            return completion.choices().get(0).message().content();
        } else {
            return "응답을 받을 수 없습니다.";
        }
    }
}
