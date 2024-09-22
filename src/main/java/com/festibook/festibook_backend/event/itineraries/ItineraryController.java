package com.festibook.festibook_backend.event.itineraries;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItineraryController {

    private final ItineraryService gptService;

    public ItineraryController(ItineraryService gptService) {
        this.gptService = gptService;
    }

    @GetMapping("/itineraries")
    public String askGpt(
            @RequestParam String departureLocation,  // 출발위치
            @RequestParam String arrivalLocation,    // 도착위치
            @RequestParam String departureDate,      // 여행 출발날짜 (MM-DD 형식)
            @RequestParam String arrivalDate,        // 여행 끝나는날짜 (MM-DD 형식)
            @RequestParam int numberOfPeople,        // 인원수
            @RequestParam String travelIntensity,    // 여행 강도 (상, 중, 하)
            @RequestParam int totalBudget,           // 총 예산 (만원 단위)
            @RequestParam(defaultValue = "3") int mealCount  // 식사 횟수 (기본값: 3회)
    ) {
        return gptService.askQuestion(departureLocation, arrivalLocation, departureDate, arrivalDate, numberOfPeople, travelIntensity, totalBudget, mealCount);
    }
}
