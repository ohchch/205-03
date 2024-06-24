package com.example.billingservice.controllers;

import com.example.billingservice.model.User;
import com.example.billingservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        logger.info("Received login request with email: {}", email);

        boolean isAuthenticated = userService.authenticate(email, password);

        if (isAuthenticated) {
            return "redirect:/stores/all";
        } else {
            if (userService.findByEmail(email) == null) {
                model.addAttribute("errorMessage", "User does not exist.");
            } else {
                model.addAttribute("errorMessage", "Invalid password.");
            }
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // 对应 register.html 模板
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.saveUser(user);
            model.addAttribute("message", "User registered successfully!");
            return "success"; // 对应 success.html 模板
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // 返回 register 页面
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to register user.");
            return "register"; // 返回 register 页面
        }
    }

    @GetMapping("/endpoint")
    public String endpoint() {
        return "endpoint";
    }
}
