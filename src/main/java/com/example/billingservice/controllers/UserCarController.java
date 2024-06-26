package com.example.billingservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // 更改这里的导入
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.services.UserService;

@Controller
@RequestMapping("/user")
public class UserCarController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/cars")
    public String getUserCars(@PathVariable Long userId, Model model) {
        try {
            List<CarDTO> userCars = userService.getUserCars(userId);
            model.addAttribute("cars", userCars);
            return "usercarlist";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
