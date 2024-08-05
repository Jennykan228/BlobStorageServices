package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.AzureBlobOperation;

@RestController
public class CAzureStorage {

    @Autowired
    private AzureBlobOperation operate;    

    @GetMapping("/createContainer")
    public void createContainer(){       
        operate.createContainer();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String url = operate.write(file);
        return ResponseEntity.ok("File uploaded successfully. URL: " + url);
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> ListAllItems(){       
        return ResponseEntity.ok(operate.ListAllItems());
    }

    @PutMapping("/update/{blobName}")
    public ResponseEntity<String> updateFile(@PathVariable String blobName, @RequestParam("file") MultipartFile file) throws IOException {
        String url = operate.updateFile(blobName, file);
        return ResponseEntity.ok("File updated successfully. URL: " + url);
    }
    
    @DeleteMapping("/delete/{blobName}")
    public ResponseEntity<String> deleteFile(@PathVariable String blobName) {
        operate.delete(blobName);
        return ResponseEntity.ok("File deleted successfully");
    }

}
