package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public interface IAzureStorage {
    String write(MultipartFile file);

    String updateFile(String blobFileUrl, MultipartFile file);

    List<String> ListAllItems();

    void delete(String blobFileUrl);

    void createContainer();
    
}