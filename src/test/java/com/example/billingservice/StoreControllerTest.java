package com.example.billingservice;

import com.example.billingservice.controllers.StoreController;
import com.example.billingservice.dto.StoreDTO;
import com.example.billingservice.services.StoreService;
import com.example.billingservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

@WebMvcTest(StoreController.class)
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"Administrator"})
    public void testGetAllStores() throws Exception {
        // 模拟storeService返回的数据
        Mockito.when(storeService.getAllStores()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/stores/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("storeList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("stores"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"Administrator"})
    public void testShowAddStoreForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stores/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("addStore"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("store"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"Administrator"})
    public void testAddStore_Success() throws Exception {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setName("Test Store");

        Mockito.doNothing().when(storeService).addStore(Mockito.any(StoreDTO.class));
        Mockito.doNothing().when(userService).saveUser(Mockito.any());

        mockMvc.perform(MockMvcRequestBuilders.post("/stores/add")
                .param("name", "Test Store")
                .param("email", "test@example.com")
                .param("password", "password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("success"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", "Store and User added successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"Administrator"})
    public void testShowEditStoreForm() throws Exception {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(1L);
        storeDTO.setName("Test Store");

        Mockito.when(storeService.getStoreById(1L)).thenReturn(storeDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/stores/edit/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("store"))
                .andExpect(MockMvcResultMatchers.model().attribute("store", storeDTO));
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"Administrator"})
    public void testUpdateStore_Success() throws Exception {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(1L);
        storeDTO.setName("Updated Store");

        Mockito.doNothing().when(storeService).updateStore(Mockito.any(StoreDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/stores/edit/1")
                .param("name", "Updated Store"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("edit"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", "Store updated successfully!"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"Administrator"})
    public void testDeleteStore_Success() throws Exception {
        Mockito.doNothing().when(storeService).deleteStoreById(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/stores/delete/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/stores/all"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Store deleted successfully!"));
    }

    @Test
    public void testAccessDeniedPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stores/access-denied"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("access-denied"));
    }
}
