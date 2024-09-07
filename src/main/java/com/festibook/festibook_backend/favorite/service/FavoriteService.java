package com.festibook.festibook_backend.favorite.service;

import com.festibook.festibook_backend.favorite.dto.FavoriteEventResponseDto;
import com.festibook.festibook_backend.event.entity.Event;
import com.festibook.festibook_backend.event.repository.EventRepository;
import com.festibook.festibook_backend.jwt.TokenService;
import com.festibook.festibook_backend.user.entity.User;
import com.festibook.festibook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EventRepository eventRepository;

    public FavoriteEventResponseDto addFavorite(Long eventId, HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> heartEvents = getHeartEventsAsList(user.getHeartEvents());
        if (heartEvents.contains(eventId)) {
            throw new RuntimeException("이미 즐겨찾기 목록에 존재하는 이벤트 입니다.");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        heartEvents.add(eventId);
        user.setHeartEvents(heartEvents.stream().map(String::valueOf).collect(Collectors.joining(",")));
        userRepository.save(user);

        return convertToFavoriteResponse(event);
    }

    public List<FavoriteEventResponseDto> getFavoriteList(HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> heartEvents = getHeartEventsAsList(user.getHeartEvents());
        List<Event> favoriteEvents = eventRepository.findAllById(heartEvents);

        return favoriteEvents.stream()
                .map(this::convertToFavoriteResponse)
                .collect(Collectors.toList());
    }

    public void removeFavorite(Long eventId, HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> heartEvents = getHeartEventsAsList(user.getHeartEvents());
        if (!heartEvents.contains(eventId)) {
            throw new RuntimeException("이벤트가 즐겨찾기 목록에 존재하지 않습니다.");
        }

        heartEvents.remove(eventId);
        user.setHeartEvents(heartEvents.stream().map(String::valueOf).collect(Collectors.joining(",")));
        userRepository.save(user);
    }

    private List<Long> getHeartEventsAsList(String heartEvents) {
        if (heartEvents == null || heartEvents.isEmpty()) {
            return new ArrayList<>();
        }
        return List.of(heartEvents.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
    }

    private FavoriteEventResponseDto convertToFavoriteResponse(Event event) {
        return FavoriteEventResponseDto.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .address1(event.getAddress1())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .thumbnailUrl(event.getThumbnailUrl())
                .build();
    }
}
