package com.ll.TeamProject.domain.user.controller;

import com.ll.TeamProject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${custom.dev.backUrl}")
    String devBackUrl;

    @GetMapping("/login")
    public String login(Model model) {
        String location = devBackUrl+"/oauth2/authorization/kakao";
        model.addAttribute("location", location);
        return "login";
    }

    @GetMapping("/index")
    public String index( ) {
        return "index";
    }
}
