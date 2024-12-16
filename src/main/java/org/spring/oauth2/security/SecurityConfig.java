package org.spring.oauth2.security;

import lombok.RequiredArgsConstructor;
import org.spring.oauth2.service.SocialAppService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final SocialAppService appService;

//    @Bean
//    @Lazy
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .oauth2Login(oauth2Login -> oauth2Login
//                        .loginPage("/")
//                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
//                                .userService(appService))
//                        .defaultSuccessUrl("/user")
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/")
//                        .invalidateHttpSession(true)
//                        .clearAuthentication(true)
//                        .deleteCookies("JSESSIONID")
//                );
//        return http.build();
//    }
}

























