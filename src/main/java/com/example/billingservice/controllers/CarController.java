package com.example.billingservice.controllers;

import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.Car;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.repositories.UserRepository;
import com.example.billingservice.services.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/add")
    public String showAddCarForm(Model model, Authentication authentication) {
        model.addAttribute("carDTO", new CarDTO());

        if (authentication != null) {
            User principal = (User) authentication.getPrincipal();
            String userEmail = principal.getUsername();

            com.example.billingservice.entities.User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user != null) {
                Long userId = user.getId();
                model.addAttribute("userId", userId);
            } else {
                model.addAttribute("errorMessage", "User not found for email: " + userEmail);
            }
        }

        return "addCar";
    }

    @PostMapping("/add")
    public String addCar(@RequestParam("userId") Long userId, @Valid @ModelAttribute("carDTO") CarDTO carDTO, BindingResult result, @RequestParam("image") MultipartFile image, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            return "addCar";
        }

        try {
            User principal = (User) authentication.getPrincipal();
            String userEmail = principal.getUsername();
            com.example.billingservice.entities.User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found or unauthorized"));
            if (!user.getId().equals(userId)) {
                throw new ResourceNotFoundException("Unauthorized");
            }

            Car car = new Car();
            car.setName(carDTO.getName());
            car.setBrand(carDTO.getBrand());
            car.setModel(carDTO.getModel());
            car.setRegistration(carDTO.getRegistration());
            car.setPrice(carDTO.getPrice());

            logger.info("Adding new car: {}", carDTO);

            carService.addCar(carDTO, image, userId);

        } catch (IOException e) {
            logger.error("Error adding car: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error adding car: " + e.getMessage());
            return "addCar";
        } catch (ResourceNotFoundException e) {
            logger.error("Error adding car: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error adding car: " + e.getMessage());
            return "addCar";
        }

        return "redirect:/cars/all";
    }

    @GetMapping("/all")
    public String getAllCars(Model model) {
        try {
            List<CarDTO> cars = carService.getAllCars();
            model.addAttribute("cars", cars);

            logger.info("Fetching all cars. Count: {}", cars.size());

        } catch (Exception e) {
            logger.error("Error fetching all cars: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error fetching all cars: " + e.getMessage());
        }

        return "carList";
    }

    @GetMapping("/edit/{id}")
    public String showEditCarForm(@PathVariable Long id, Model model) {
        try {
            CarDTO carDTO = carService.getCarById(id);
            model.addAttribute("carDTO", carDTO);
            return "edit";
        } catch (ResourceNotFoundException e) {
            logger.error("Car not found: {}", e.getMessage());
            model.addAttribute("errorMessage", "Car not found");
            return "redirect:/cars/all";
        } catch (Exception e) {
            logger.error("Error editing car: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error editing car: " + e.getMessage());
            return "redirect:/cars/all";
        }
    }

    @PostMapping("/edit/{id}")
    public String editCar(@Valid @ModelAttribute("carDTO") CarDTO carDTO, BindingResult result, @RequestParam("image") MultipartFile image, Model model) {
        if (result.hasErrors()) {
            return "edit";
        }

        try {
            carService.updateCar(carDTO, image);
        } catch (ResourceNotFoundException e) {
            logger.error("Car not found: {}", e.getMessage());
            model.addAttribute("errorMessage", "Car not found");
            return "editCar";
        } catch (IOException e) {
            logger.error("Error uploading image: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error uploading image: " + e.getMessage());
            return "editCar";
        } catch (Exception e) {
            logger.error("Error editing car: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error editing car: " + e.getMessage());
            return "editCar";
        }

        return "redirect:/cars/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteCarById(@PathVariable Long id, Model model) {
        try {
            carService.deleteCarById(id);
            model.addAttribute("message", "Car deleted successfully.");

            logger.info("Car with ID {} deleted successfully.", id);

        } catch (ResourceNotFoundException e) {
            logger.error("Car not found: {}", e.getMessage());
            model.addAttribute("errorMessage", "Car not found.");
        } catch (Exception e) {
            logger.error("Error deleting car: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error deleting car: " + e.getMessage());
        }

        return "delete";
    }

    @PostMapping("/toggleActivate/{id}")
    public String toggleActivate(@PathVariable("id") Long carId, HttpServletRequest request) {
        try {
            carService.toggleActivate(carId);
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                return "redirect:" + referer;
            } else {
                return "redirect:/cars";
            }
        } catch (ResourceNotFoundException e) {
            return "redirect:/error";
        }
    }
}
