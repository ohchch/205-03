package com.example.billingservice.controllers;

import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.Car;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.repositories.UserRepository;
import com.example.billingservice.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @Autowired  // 注入UserRepository
    private UserRepository userRepository;

    @GetMapping("/add")
    public String showAddCarForm(Model model, Authentication authentication) {
        model.addAttribute("carDTO", new CarDTO());

        // 获取当前登录用户的ID
        if (authentication != null) {
            org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String userEmail = principal.getUsername();

            // 从数据库中获取用户对象
            com.example.billingservice.entities.User user = userRepository.findByEmail(userEmail);
            if (user != null) {
                Long userId = user.getId();
                model.addAttribute("userId", userId); // 将userId添加到模型中
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
            // 获取当前登录用户
            org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String userEmail = principal.getUsername();
            com.example.billingservice.entities.User user = userRepository.findByEmail(userEmail);
            if (user == null || !user.getId().equals(userId)) {
                throw new ResourceNotFoundException("User not found or unauthorized");
            }
    
            // 创建 Car 对象并设置属性
            Car car = new Car();
            car.setName(carDTO.getName());
            car.setBrand(carDTO.getBrand());
            car.setModel(carDTO.getModel());
            car.setRegistration(carDTO.getRegistration());
            car.setPrice(carDTO.getPrice());
    
            // 调用 CarService 中的 addCar 方法来保存车辆信息
            carService.addCar(carDTO, image, userId);
    
        } catch (IOException | ResourceNotFoundException e) {
            model.addAttribute("errorMessage", "Error adding car: " + e.getMessage());
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
            return "edit";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", "Car not found");
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
            model.addAttribute("errorMessage", "Car not found");
            return "editCar";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error uploading image: " + e.getMessage());
            return "editCar";
        }

        return "redirect:/cars/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteCarById(@PathVariable Long id, Model model) {
        try {
            carService.deleteCarById(id);
            model.addAttribute("message", "Car deleted successfully.");
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", "Car not found.");
        }

        return "delete";
    }
}
