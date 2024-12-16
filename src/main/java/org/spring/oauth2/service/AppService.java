package org.spring.oauth2.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.oauth2.entity.User;
import org.spring.oauth2.repository.UserRepository;
import org.spring.oauth2.role.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class AppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        List<Role> roles = new ArrayList<>();
        String clientName = userRequest.getClientRegistration().getClientName();
        User user;
        String id = userRequest.getClientRegistration().getRegistrationId();
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.admin.name()));
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(oAuth2User, authorities, id);
        SecurityContextHolder.getContext().setAuthentication(token);
        if (clientName.equals("GitHub")) {
            if (userRepository.findByName(oAuth2User.getAttribute("login")) == null) {
                roles.add(Role.admin);
                user = new User.Builder()
                        .name(oAuth2User.getAttribute("login"))
                        .email("unknown")
                        .roles(roles).build();
                userRepository.save(user);
                log.info("The user with the login: " + oAuth2User.getAttribute("login") + " and role: ADMIN is saved");

            }
            return new DefaultOAuth2User(Collections.singletonList(new SimpleGrantedAuthority("ROLE_admin")), oAuth2User.getAttributes(), "id");
        } else {
            if (userRepository.findByName(oAuth2User.getAttribute("name")) == null) {
                roles.add(Role.user);
                user = new User.Builder()
                        .name(oAuth2User.getAttribute("name"))
                        .email(oAuth2User.getAttribute("email"))
                        .roles(roles).build();
                userRepository.save(user);
                log.info("The user with the login: " + oAuth2User.getAttribute("name") + " and role: USER, is saved");
            }
        }
        return oAuth2User;
    }
}
