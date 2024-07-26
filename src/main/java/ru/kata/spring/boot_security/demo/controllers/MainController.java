package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserDetailsServiceImpl;

import java.security.Principal;

@Controller
public class MainController {

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/user")
    public String userPage(Principal principal, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("id", user.getId());
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}
