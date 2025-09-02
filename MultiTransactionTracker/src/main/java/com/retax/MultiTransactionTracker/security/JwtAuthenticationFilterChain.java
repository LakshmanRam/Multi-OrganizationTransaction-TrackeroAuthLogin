package com.retax.MultiTransactionTracker.security;

import com.retax.MultiTransactionTracker.model.User;
import com.retax.MultiTransactionTracker.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilterChain extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilterChain.class);

    public JwtAuthenticationFilterChain(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromHeader(request);
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                Claims claims = jwtProvider.parseClaims(token);
                String userId = claims.getSubject();

                Optional<User> user = userRepository.findById(Long.valueOf(userId));
                if (user.isPresent()) {
                    UserPrincipal userPrincipal = new UserPrincipal(user.get());
                    // Create authentication token (no password needed)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException ex) {
            log.warn("JWT validation failed: {}", ex.getMessage());

        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            log.debug("Extracted JWT token from Authorization header");
            return bearer.substring(7);
        }
//        log.warn("Authorization header missing or malformed");

        return null;
    }
}