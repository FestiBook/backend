package com.festibook.festibook_backend.user.service;

import com.festibook.festibook_backend.jwt.service.TokenService;
import com.festibook.festibook_backend.user.dto.UserRequestDto;
import com.festibook.festibook_backend.user.dto.UserResponseDto;
import com.festibook.festibook_backend.user.entity.User;
import com.festibook.festibook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    private static final String NICKNAME_REGEX = "^[a-zA-Z0-9가-힣]+$";


    public UserResponseDto getUserInfo(HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return convertToUserResponseDto(user);
    }

    public UserResponseDto updateUser(UserRequestDto requestDto, HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!Pattern.matches(NICKNAME_REGEX, requestDto.getNickname())) {
            throw new RuntimeException("닉네임은 영문자와 숫자만 가능합니다.");
        }

        if (userRepository.existsByNicknameAndIdNot(requestDto.getNickname(), userId)) {
            throw new RuntimeException("중복된 닉네임입니다.");
        }
        user.setNickname(requestDto.getNickname());

        User updatedUser = userRepository.save(user);
        return convertToUserResponseDto(updatedUser);
    }

    public void deleteUser(HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    private UserResponseDto convertToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .platform(user.getPlatform())
                .build();
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        tokenService.invalidateRefreshToken(user);
    }
}
