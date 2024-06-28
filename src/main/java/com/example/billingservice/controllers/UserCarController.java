package com.example.billingservice.controllers;

import java.util.List;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.Reservation;
import com.example.billingservice.exceptions.ResourceNotFoundException;

import com.example.billingservice.services.UserService;
import com.example.billingservice.services.ReservationService;

@Controller
@RequestMapping("/user")
public class UserCarController {

    private final UserService userService;
    private final ReservationService reservationService;

    public UserCarController(UserService userService,ReservationService reservationService) {
        this.userService = userService;

        this.reservationService = reservationService;
    }

    @GetMapping("/{userId}/cars")
    public String getUserCars(@PathVariable Long userId, Model model) {
        try {
            List<CarDTO> userCars = userService.getUserCars(userId);

            // Iterate over userCars to fetch reservations for each car
            for (CarDTO car : userCars) {
                List<Reservation> reservations = reservationService.getReservationsForCar(car.getId());
                car.setReservations(reservations);
            }

            model.addAttribute("cars", userCars);
            return "usercarlist";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{userId}/profile")
    public String getUserProfile(@PathVariable Long userId, Model model) {
        try {
            com.example.billingservice.entities.User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            return "userprofile";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/change-email")
    public String changeEmail(@RequestParam Long userId, @RequestParam String newEmail, Model model) {
        try {
            userService.changeEmail(userId, newEmail);
            return "redirect:/user/" + userId + "/profile";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam Long userId, @RequestParam String newPassword, Model model) {
        try {
            userService.changePassword(userId, newPassword);
            return "redirect:/user/" + userId + "/profile";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }


}
