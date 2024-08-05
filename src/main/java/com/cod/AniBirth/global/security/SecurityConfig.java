package com.cod.AniBirth.global.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private OAuth2UserService oAuth2UserService;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .cors().disable()
//                .csrf().disable()
//                .sessionManagement( sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(STATELESS)
//                )
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/donation/**")).authenticated() // /donation/** 경로는 인증 필요
                        .requestMatchers(new AntPathRequestMatcher("/article/**")).permitAll())
                .formLogin((formLogin) -> formLogin
                        .loginPage("/member/login")
                        .defaultSuccessUrl("/")
                        .failureHandler(customAuthenticationFailureHandler))
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/member/login")
                                .userInfoEndpoint(
                                        userInfoEndpoint -> userInfoEndpoint
                                                .userService(oAuth2UserService)
                                )
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
