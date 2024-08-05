package com.example.demo.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record Request(
        String firstName,
        String lastName,
        String userName,
        String email,
        String password,
        MultipartFile file
        
) {
}
