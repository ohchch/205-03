package com.example.billingservice.controllers;

import com.example.billingservice.entities.User;

import com.example.billingservice.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            if (error.equals("invalidEmail")) {
                model.addAttribute("error", "Invalid email address.");
            } else if (error.equals("invalidPassword")) {
                model.addAttribute("error", "Invalid password.");
            }
        }
        return "login";
    }
    

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        User user = userService.findByEmail(email);
        
        if (user == null) {
            return "redirect:/login?error=invalidEmail";
        }
    
        boolean isAuthenticated = userService.authenticate(email, password);
    
        if (isAuthenticated) {
            session.setAttribute("userId", user.getId());
            return "redirect:/user/" + user.getId() + "/profile";
        } else {
            return "redirect:/login?error=invalidPassword";
        }
    }
    
    
    
    

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.saveUser(user);
            model.addAttribute("message", "User registered successfully!");
            return "success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to register user.");
            return "register";
        }
    }


    

    @GetMapping("/endpoint")
    public String endpoint() {
        return "endpoint";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/")
    public String homepage() {
        return "index";
    }
}
