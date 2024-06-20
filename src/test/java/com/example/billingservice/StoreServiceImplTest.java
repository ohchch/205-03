package com.example.billingservice;

import com.example.billingservice.dto.StoreDTO;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.repositories.StoreRepository;
import com.example.billingservice.services.StoreServiceImpl;
import com.example.billingservice.services.UserService;
import com.example.billingservice.stores.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private StoreServiceImpl storeService;

    private Store store;

    @BeforeEach
    public void setUp() {
        store = new Store();
        store.setId(1L);
        store.setName("Test Store");
        store.setEmail("test@store.com");
        store.setPhoneNumber("1234567890");
        store.setCity("Test City");
        // Initialize other properties as needed
    }

    @Test
    public void testGetAllStores() {
        List<Store> stores = new ArrayList<>();
        stores.add(store);

        when(storeRepository.findAll()).thenReturn(stores);

        List<StoreDTO> storeDTOs = storeService.getAllStores();

        assertNotNull(storeDTOs);
        assertEquals(1, storeDTOs.size());
        assertEquals(store.getName(), storeDTOs.get(0).getName());
    }

    @Test
    public void testAddStore() {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setName("New Store");
        storeDTO.setEmail("new@store.com");
        storeDTO.setPhoneNumber("9876543210");
        storeDTO.setCity("New City");

        when(storeRepository.existsByNameOrPhoneNumberOrEmail(storeDTO.getName(), storeDTO.getPhoneNumber(), storeDTO.getEmail())).thenReturn(false);

        storeService.addStore(storeDTO);

        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    public void testUpdateStore() {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(1L);
        storeDTO.setName("Updated Store");
        storeDTO.setEmail("updated@store.com");
        storeDTO.setPhoneNumber("9876543210");
        storeDTO.setCity("Updated City");

        storeService.updateStore(storeDTO);

        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    public void testDeleteStoreById() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        assertDoesNotThrow(() -> storeService.deleteStoreById(1L));

        verify(userService, times(1)).deleteUserByEmail(store.getEmail());
        verify(storeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteStoreById_StoreNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> storeService.deleteStoreById(1L));
    }

    @Test
    public void testGetStoreById() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        StoreDTO storeDTO = storeService.getStoreById(1L);

        assertNotNull(storeDTO);
        assertEquals(store.getId(), storeDTO.getId());
        assertEquals(store.getName(), storeDTO.getName());
        assertEquals(store.getEmail(), storeDTO.getEmail());
    }

    @Test
    public void testGetStoreById_StoreNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> storeService.getStoreById(1L));
    }
}
