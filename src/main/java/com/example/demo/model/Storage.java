package com.example.demo.model;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Storage {
    private String path;
    private String fileName;
    private InputStream inputStream;
    
    
}
