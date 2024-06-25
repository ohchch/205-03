package com.example.billingservice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.File;

@RestController
public class FileUploadController {

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("请选择文件上传.");
            }

            // 获取文件名并保存到上传目录
            String fileName = file.getOriginalFilename();
            File dest = new File(uploadDir + File.separator + fileName);
            file.transferTo(dest);

            return ResponseEntity.ok("文件上传成功: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败: " + e.getMessage());
        }
    }
}
