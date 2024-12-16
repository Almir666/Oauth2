package org.spring.oauth2.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.oauth2.entity.User;
import org.spring.oauth2.repository.UserRepository;
import org.spring.oauth2.role.Role;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        List<Role> roles = new ArrayList<>();
        String clientName = userRequest.getClientRegistration().getClientName();
        User user;
        if(clientName.equals("GitHub")) {
            if(userRepository.findByName(oAuth2User.getAttribute("login")) == null) {
                roles.add(Role.admin);
                user = new User.Builder()
                        .name(oAuth2User.getAttribute("login"))
                        .email("unknown")
                        .roles(roles).build();
                userRepository.save(user);
                log.info("The user with the login: " + oAuth2User.getAttribute("login") + " and role: ADMIN is saved");
            }
        } else {
            if(userRepository.findByName(oAuth2User.getAttribute("name")) == null) {
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
