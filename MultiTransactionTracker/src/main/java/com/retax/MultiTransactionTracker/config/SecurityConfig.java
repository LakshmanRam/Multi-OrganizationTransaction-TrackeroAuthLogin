package com.retax.MultiTransactionTracker.config;

import com.retax.MultiTransactionTracker.repository.UserRepository;
import com.retax.MultiTransactionTracker.security.JwtAuthenticationFilterChain;
import com.retax.MultiTransactionTracker.security.JwtProvider;
import com.retax.MultiTransactionTracker.security.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public SecurityConfig(JwtProvider jwtProvider, UserRepository userRepository, OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public JwtAuthenticationFilterChain jwtAuthenticationFilter() {
        return new JwtAuthenticationFilterChain(jwtProvider, userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth2/**", "/auth/**", "/api/**", "/login/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

