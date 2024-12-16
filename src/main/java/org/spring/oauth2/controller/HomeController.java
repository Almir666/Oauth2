package org.spring.oauth2.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class HomeController {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public String adminPage() {
        return "Page only for admins";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("login", principal.getAttribute("login"));
        model.addAttribute("id", principal.getAttribute("id"));
        model.addAttribute("email", principal.getAttribute("email"));
        return model.getAttribute("name") + " "
                + model.getAttribute("login") + " "
                + model.getAttribute("email") + " "
                + model.getAttribute("id");
    }

}
