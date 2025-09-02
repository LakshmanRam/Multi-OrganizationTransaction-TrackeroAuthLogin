package com.retax.MultiTransactionTracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retax.MultiTransactionTracker.model.User;
import com.retax.MultiTransactionTracker.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    public OAuth2SuccessHandler(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
//        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
//        String token = jwtProvider.generateToken(principal);


        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttribute("email");
        String name = (String) oAuth2User.getAttribute("name");
log.info("Details of User: {},{}",email,name);
        // find or create user
        User existingUser = userRepository.findByEmail(email)
        .orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            return userRepository.save(newUser);
        });

        String token = jwtProvider.generateToken(existingUser.getId().toString(), existingUser.getEmail());
        log.info("JWT issued for user {} (id = {}: {}",existingUser.getEmail(),existingUser.getId(),token);
        
        Map<String, Object> payload = Map.of(
                "token", token,
                "user", Map.of(
                        "id", existingUser.getId(),
                        "email", existingUser.getEmail(),
                        "name", existingUser.getName()
                )
        );
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().write(objectMapper.writeValueAsString(payload));
        response.getWriter().flush();
    }
}
