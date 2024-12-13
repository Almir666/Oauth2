package org.spring.oauth2.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.oauth2.entity.User;
import org.spring.oauth2.repository.UserRepository;
import org.spring.oauth2.role.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        List<Role> roles = new ArrayList<>();
        String clientName = userRequest.getClientRegistration().getClientName();
        if(clientName.equals("GitHub")) {
                roles.add(Role.ADMIN);
        } else {
            roles.add(Role.USER);
        }

        User user = new User.Builder()
                .name(oAuth2User.getAttribute("name"))
                .login(oAuth2User.getAttribute("login"))
                .email(oAuth2User.getAttribute("email"))
                .roles(roles).build();
        userRepository.save(user);

        return oAuth2User;
    }
}
