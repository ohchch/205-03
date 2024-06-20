package com.example.billingservice.controllers;

import com.example.billingservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        logger.info("Received login request with email: {}", email);
        logger.debug("Password: {}", password);

        // 调用 UserService 的 authenticate 方法进行认证
        boolean isAuthenticated = userService.authenticate(email, password);

        if (isAuthenticated) {
            // 认证成功，重定向到商店列表页
            return "redirect:/stores/all";
        } else {
            // 如果认证失败，进一步判断是用户不存在还是密码错误
            if (userService.findByEmail(email) == null) {
                // 用户不存在
                model.addAttribute("errorMessage", "User does not exist.");
            } else {
                // 密码错误
                model.addAttribute("errorMessage", "Invalid password.");
            }
            return "login";
        }
    }

    @GetMapping("/endpoint")
    public String endpoint() {
        return "endpoint";
    }
}
