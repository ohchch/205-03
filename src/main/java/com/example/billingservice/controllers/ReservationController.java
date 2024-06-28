package com.example.billingservice.controllers;

import com.example.billingservice.entities.Reservation;
import com.example.billingservice.services.CustomUserDetails;
import com.example.billingservice.services.ReservationService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/make")
    public String makeReservation(@RequestParam Long carId, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();

            reservationService.makeReservation(carId, userId);
            model.addAttribute("message", "Reservation made successfully");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/cars/all";
    }

    @GetMapping("/car/{carId}")
    public String getReservationsForCar(@PathVariable Long carId, Model model) {
        List<Reservation> reservations = reservationService.getReservationsForCar(carId);
        model.addAttribute("reservations", reservations);
        return "car_reservations";
    }

    @PostMapping("/delete")
    public String deleteReservation(@RequestParam("reservationId") Long reservationId, HttpServletRequest request) {
        reservationService.deleteReservation(reservationId);
        
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/car-list"; 
        }
    }
}


