package com.example.billingservice.controllers;

import com.example.billingservice.dto.StoreDTO;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/stores")
public class StoreController {

    // Inject the StoreService bean
    @Autowired
    private StoreService storeService;


    // Handle GET requests to fetch all stores
    @GetMapping("/all")
    public String getAllStores(Model model) {
        List<StoreDTO> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        return "storeList"; // Corresponds to the storeList.html template
    }

    // Handle GET requests to show the form for adding a new store
    @GetMapping("/add")
    public String showAddStoreForm(Model model) {
        model.addAttribute("store", new StoreDTO());
        return "addStore"; // Corresponds to the addStore.html template
    }

    // Handle POST requests to add a new store
    @PostMapping("/add")
    public String addStore(@ModelAttribute("store") StoreDTO storeDTO,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        try {
            // Add the store logic
            storeService.addStore(storeDTO);
            model.addAttribute("message", "Store and User added successfully!");
            return "success"; // Corresponds to the success.html template for showing the success message
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "addStore"; // Return to the add store page
        } catch (Exception e) {
            model.addAttribute("message", "Failed to add store and user.");
            return "addStore"; // Return to the add store page
        }
    }

    // Handle GET requests to show the form for editing an existing store
    @GetMapping("/edit/{id}")
    public String showEditStoreForm(@PathVariable Long id, Model model) {
        try {
            StoreDTO storeDTO = storeService.getStoreById(id);
            model.addAttribute("store", storeDTO);
            return "edit"; // Corresponds to the edit.html template
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", "Store not found.");
            return "error"; // Corresponds to the error.html template for showing error messages
        } catch (Exception e) {
            model.addAttribute("message", "Failed to retrieve store.");
            return "error"; // Corresponds to the error.html template for showing error messages
        }
    }

    // Handle POST requests to update an existing store
    @PostMapping("/edit/{id}")
    public String updateStore(@PathVariable Long id,
                              @ModelAttribute("store") StoreDTO storeDTO,
                              Model model) {
        try {
            storeDTO.setId(id); // Ensure the ID is consistent
            storeService.updateStore(storeDTO);
            model.addAttribute("message", "Store updated successfully!");
            return "edit"; // Corresponds to the edit.html template
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", "Store not found.");
            return "error"; // Corresponds to the error.html template for showing error messages
        } catch (Exception e) {
            model.addAttribute("message", "Failed to update store.");
            return "error"; // Corresponds to the error.html template for showing error messages
        }
    }

    // Handle POST requests to delete an existing store
    @PostMapping("/delete/{id}")
    public String deleteStore(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            storeService.deleteStoreById(id); // Call the deleteStoreById method
            redirectAttributes.addFlashAttribute("message", "Store deleted successfully!");
            return "redirect:/stores/all"; // Redirect to the list of all stores
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", "Store not found.");
            return "error"; // Corresponds to the error.html template for showing error messages
        } catch (Exception e) {
            model.addAttribute("message", "Failed to delete store.");
            return "error"; // Corresponds to the error.html template for showing error messages
        }
    }

    // Handle GET requests for access denied page
    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied"; // Corresponds to the access-denied.html template
    }

    // Handle POST requests for access denied page
    @PostMapping("/access-denied")
    public String handlePostAccessDenied() {
        return "access-denied"; // Corresponds to the access-denied.html template
    }
}
