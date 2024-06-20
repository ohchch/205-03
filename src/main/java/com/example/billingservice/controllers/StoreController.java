package com.example.billingservice.controllers;

import com.example.billingservice.dto.StoreDTO;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.services.StoreService;
import com.example.billingservice.services.UserService;
import com.example.billingservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getAllStores(Model model) {
        List<StoreDTO> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        return "storeList"; // 对应模板文件名为 storeList.html
    }

    @GetMapping("/add")
    public String showAddStoreForm(Model model) {
        model.addAttribute("store", new StoreDTO());
        return "addStore"; // 对应模板文件名为 addStore.html
    }

    @PostMapping("/add")
    public String addStore(@ModelAttribute("store") StoreDTO storeDTO,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        try {
            // 添加商店逻辑
            storeService.addStore(storeDTO);
    
            // 创建并保存 User 到数据库
            User user = new User(email, password); // 这里假设User类有相应的构造方法
            userService.saveUser(user); // 调用保存用户的方法
    
            model.addAttribute("message", "Store and User added successfully!");
            return "success"; // 对应模板文件名为 success.html，用于显示添加成功的页面
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "addStore"; // 返回添加商店的页面
        } catch (Exception e) {
            model.addAttribute("message", "Failed to add store and user.");
            return "addStore"; // 返回添加商店的页面
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditStoreForm(@PathVariable Long id, Model model) {
        try {
            StoreDTO storeDTO = storeService.getStoreById(id);
            model.addAttribute("store", storeDTO);
            return "edit"; // 对应模板文件名为 edit.html
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", "Store not found.");
            return "error"; // 对应模板文件名为 error.html，用于显示错误信息
        } catch (Exception e) {
            model.addAttribute("message", "Failed to retrieve store.");
            return "error"; // 对应模板文件名为 error.html，用于显示错误信息
        }
    }

    @PostMapping("/edit/{id}")
    public String updateStore(@PathVariable Long id,
                              @ModelAttribute("store") StoreDTO storeDTO,
                              Model model) {
        try {
            storeDTO.setId(id); // 确保 ID 一致性
            storeService.updateStore(storeDTO);
            model.addAttribute("message", "Store updated successfully!");
            return "edit"; // 对应模板文件名为 edit.html
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", "Store not found.");
            return "error"; // 对应模板文件名为 error.html，用于显示错误信息
        } catch (Exception e) {
            model.addAttribute("message", "Failed to update store.");
            return "error"; // 对应模板文件名为 error.html，用于显示错误信息
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteStore(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            storeService.deleteStoreById(id); // 调用 deleteStoreById 方法
            redirectAttributes.addFlashAttribute("message", "Store deleted successfully!");
            return "redirect:/stores/all"; // 重定向到所有商店列表页面
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", "Store not found.");
            return "error"; // 对应模板文件名为 error.html，用于显示错误信息
        } catch (Exception e) {
            model.addAttribute("message", "Failed to delete store.");
            return "error"; // 对应模板文件名为 error.html，用于显示错误信息
        }
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied";
    }

    @PostMapping("/access-denied")
    public String handlePostAccessDenied() {
        return "access-denied";
    }
}
