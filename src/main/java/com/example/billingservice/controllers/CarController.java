package com.example.billingservice.controllers;

import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/add")
    public String showAddCarForm(Model model) {
        model.addAttribute("carDTO", new CarDTO());
        return "addCar";
    }

    @PostMapping("/add")
    public String addCar(@Valid @ModelAttribute("carDTO") CarDTO carDTO, BindingResult result, @RequestParam("image") MultipartFile image, Model model) {
        if (result.hasErrors()) {
            return "addCar";
        }

        try {
            carService.addCar(carDTO, image);
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error uploading image: " + e.getMessage());
            return "addCar";
        }

        return "redirect:/cars/all";
    }

    @GetMapping("/all")
    public String getAllCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "carList";
    }

    @GetMapping("/edit/{id}")
    public String showEditCarForm(@PathVariable Long id, Model model) {
        try {
            CarDTO carDTO = carService.getCarById(id);
            model.addAttribute("carDTO", carDTO);
            return "editCar";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", "Car not found");
            return "redirect:/cars/all";
        }
    }

    @PostMapping("/edit")
    public String editCar(@Valid @ModelAttribute("carDTO") CarDTO carDTO, BindingResult result, @RequestParam("image") MultipartFile image, Model model) {
        if (result.hasErrors()) {
            return "editCar";
        }

        try {
            carService.updateCar(carDTO, image);
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", "Car not found");
            return "editCar";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error uploading image: " + e.getMessage());
            return "editCar";
        }

        return "redirect:/cars/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id, Model model) {
        try {
            carService.deleteCarById(id);
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", "Car not found");
        }

        return "redirect:/cars/all";
    }
}
