package com.example.billingservice.services;

import com.example.billingservice.dto.StoreDTO;
import java.util.List;

// Interface for the StoreService
public interface StoreService {
    
    // Method to get a list of all stores
    List<StoreDTO> getAllStores();
    
    // Method to add a new store
    void addStore(StoreDTO storeDTO);
    
    // Method to update an existing store
    void updateStore(StoreDTO storeDTO);
    
    // Method to delete a store by its ID
    void deleteStoreById(Long id); 
    
    // Method to get a store by its ID
    StoreDTO getStoreById(Long id);
}
