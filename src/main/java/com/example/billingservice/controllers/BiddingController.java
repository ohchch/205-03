package com.example.billingservice.controllers;

import com.example.billingservice.entities.Bidding;
import com.example.billingservice.entities.Car;
import com.example.billingservice.entities.User;
import com.example.billingservice.repositories.BiddingRepository;
import com.example.billingservice.repositories.CarRepository;
import com.example.billingservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BiddingController {

    @Autowired
    private BiddingRepository biddingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/bidding/submit")
    public String submitBid(@RequestParam Long carId, @RequestParam Double biddingPrice, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + carId));
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
    
        Bidding highestBid = biddingRepository.findTopByCarOrderByBiddingPriceDesc(car);
    
        if (highestBid == null || biddingPrice > highestBid.getBiddingPrice()) {
            if (highestBid == null) {
                highestBid = new Bidding();
            }
            highestBid.setCar(car);
            highestBid.setBiddingPrice(biddingPrice);
            highestBid.setUser(user);
            biddingRepository.save(highestBid);
            redirectAttributes.addFlashAttribute("message", "Bid placed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Your bid must be higher than the current highest bid.");
        }
    
        return "redirect:/cars/all";
    }
    
}
