package com.example.billingservice.services;

import com.example.billingservice.dto.StoreDTO;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.repositories.StoreRepository;
import com.example.billingservice.stores.Store;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Mark this class as a Spring service
@Service
public class StoreServiceImpl implements StoreService {

    // Inject the StoreRepository bean
    private final StoreRepository storeRepository;
    // Inject the UserService bean
    private final UserService userService;

    // Constructor-based dependency injection
    public StoreServiceImpl(StoreRepository storeRepository, UserService userService) {
        this.storeRepository = storeRepository;
        this.userService = userService;
    }

    // Method to get all stores and convert them to DTOs
    @Override
    public List<StoreDTO> getAllStores() {
        return storeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Method to add a new store
    @Override
    public void addStore(StoreDTO storeDTO) {
        // Check if a store with the same name, phone number, or email already exists
        if (storeRepository.existsByNameOrPhoneNumberOrEmail(storeDTO.getName(), storeDTO.getPhoneNumber(), storeDTO.getEmail())) {
            throw new IllegalArgumentException("Store with the same name, phone number, or email already exists.");
        }

        // Convert the StoreDTO to a Store entity and save it
        Store store = convertToEntity(storeDTO);
        storeRepository.save(store);
    }

    // Method to update an existing store
    @Override
    public void updateStore(StoreDTO storeDTO) {
        // Convert the StoreDTO to a Store entity and save it (update if exists)
        Store store = convertToEntity(storeDTO);
        storeRepository.save(store);
    }

    // Method to delete a store by its ID
    @Override
    public void deleteStoreById(Long id) {
        // Find the store by its ID or throw an exception if not found
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));

        // Assuming store email is the same as user's email, delete the user by email
        userService.deleteUserByEmail(store.getEmail());

        // Delete the store by its ID
        storeRepository.deleteById(id);
    }

    // Method to get a store by its ID and convert it to a DTO
    @Override
    public StoreDTO getStoreById(Long id) {
        Optional<Store> optionalStore = storeRepository.findById(id);
        Store store = optionalStore.orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));
        return convertToDTO(store);
    }

    // Helper method to convert a Store entity to a StoreDTO
    private StoreDTO convertToDTO(Store store) {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(store.getId());
        storeDTO.setName(store.getName());
        storeDTO.setPhoneNumber(store.getPhoneNumber());
        storeDTO.setEmail(store.getEmail());
        storeDTO.setUnitNumber(store.getUnitNumber());
        storeDTO.setStreetName(store.getStreetName());
        storeDTO.setResidentialArea(store.getResidentialArea());
        storeDTO.setPostalCode(store.getPostalCode());
        storeDTO.setCity(store.getCity());
        storeDTO.setState(store.getState());
        storeDTO.setCountry(store.getCountry());
        return storeDTO;
    }

    // Helper method to convert a StoreDTO to a Store entity
    private Store convertToEntity(StoreDTO storeDTO) {
        Store store = new Store();
        store.setId(storeDTO.getId());
        store.setName(storeDTO.getName());
        store.setPhoneNumber(storeDTO.getPhoneNumber());
        store.setEmail(storeDTO.getEmail());
        store.setUnitNumber(storeDTO.getUnitNumber());
        store.setStreetName(storeDTO.getStreetName());
        store.setResidentialArea(storeDTO.getResidentialArea());
        store.setPostalCode(storeDTO.getPostalCode());
        store.setCity(storeDTO.getCity());
        store.setState(storeDTO.getState());
        store.setCountry(storeDTO.getCountry());
        return store;
    }
}
