package com.example.billingservice.services;

import com.example.billingservice.dto.StoreDTO;
import java.util.List;

public interface StoreService {
    List<StoreDTO> getAllStores();
    void addStore(StoreDTO storeDTO);
    void updateStore(StoreDTO storeDTO);
    void deleteStoreById(Long id); 
    StoreDTO getStoreById(Long id);
}
