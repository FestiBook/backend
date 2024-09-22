package com.festibook.festibook_backend.oauth2.handler;

import com.festibook.festibook_backend.jwt.service.TokenService;
import com.festibook.festibook_backend.user.entity.User;
import com.festibook.festibook_backend.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = determinePlatform(authentication);
        String name = extractName(registrationId, oAuth2User.getAttributes());

        if (name == null) {
            log.error("Failed to extract name from OAuth2User attributes.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to extract user name.");
            return;
        }

        User user = findOrCreateUser(name, registrationId);

        String accessToken = tokenService.generateAccessToken(user.getId());
        String refreshToken = tokenService.generateRefreshToken(user);

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect")
                .queryParam("a", accessToken)
                .queryParam("r", refreshToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    private String determinePlatform(Authentication authentication) {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        return authToken.getAuthorizedClientRegistrationId();
    }

    private String extractName(String registrationId, Map<String, Object> attributes) {
        switch (registrationId) {
            case "naver":
                return (String) attributes.get("nickname");
            case "kakao":
                Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");
                return (String) profile.get("nickname");
            case "google":
                return (String) attributes.get("name");
            default:
                return null;
        }
    }

    private User findOrCreateUser(String name, String registrationId) {
        return userRepository.findByNickname(name).orElseGet(() -> {
            User newUser = User.builder()
                    .nickname(name)
                    .platform(registrationId)
                    .build();
            userRepository.save(newUser);
            return newUser;
        });
    }
}
