package com.example.demo.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobItem;
import com.example.demo.configuration.AzureBlobStorageConfig;
import com.example.demo.dto.Request;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AzureBlobOperation implements IAzureStorage {
     
    @Autowired
    AzureBlobStorageConfig blobServiceClient;
    
    @Value("${azure.storage.container.name}")
        private String containerName;


    @Override
    public void createContainer() {
        try {
            blobServiceClient.getBlobServiceClient().createBlobContainer(containerName);           
            log.info("container created");
        } catch (Exception e) {
            e.getStackTrace();
            
        }
    
    }

    @Override
    public String write(MultipartFile file) {
        String filename = file.getOriginalFilename();
        BlobContainerClient blobContainerClient = blobServiceClient
                      .getBlobServiceClient()
                      .getBlobContainerClient(containerName);      

        BlobClient blobClient = blobContainerClient.getBlobClient(filename);

        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());

        try {
            blobClient.upload(file.getInputStream(), file.getSize(), true);
        } catch (IOException ex) {
            System.err.printf("Failed to upload from file: %s%n", ex.getMessage());
        }
       
        blobClient.setHttpHeaders(headers);
       
       return blobClient.getBlobUrl();
    }

    public String updateFile(String blobFileUrl, MultipartFile file) {
        String blobName = extractBlobNameFromUrl(blobFileUrl);

        BlobContainerClient blobContainerClient = blobServiceClient
                .getBlobServiceClient()
                .getBlobContainerClient(containerName);
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());

        try {
            blobClient.upload(file.getInputStream(), file.getSize(), true);
        } catch (Exception e) {
            System.err.printf("Failed to update: %s%n", e.getMessage());
        }

        blobClient.setHttpHeaders(headers);

        return blobClient.getBlobUrl();
    }

    @Override
    public List<String> ListAllItems() {

    BlobContainerClient blobContainerClient = blobServiceClient
                 .getBlobServiceClient()
                 .getBlobContainerClient(containerName); 

        PagedIterable<BlobItem> blobList = blobContainerClient.listBlobs();

        List<String> blobNameList = new ArrayList<>();
        for (BlobItem blob : blobList) {
            blobNameList.add(blob.getName());
        }

        return blobNameList;
    
     }

    @Override
    public void delete(String blobFileUrl) {
        String blobName = extractBlobNameFromUrl(blobFileUrl);
        BlobContainerClient blobContainerClient = blobServiceClient
                 .getBlobServiceClient()
                 .getBlobContainerClient(containerName); 

        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        blobClient.delete();

        log.info("blod deleted successfully");
       
    }
    
    private String extractBlobNameFromUrl(String blobFileUrl) {
        // Assuming the URL format is like: https://<account>.blob.core.windows.net/<container>/<blobName>
        return blobFileUrl.substring(blobFileUrl.lastIndexOf('/') + 1);
    }
 
}
