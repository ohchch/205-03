package com.example.billingservice;

import com.example.billingservice.controllers.UserController;
import com.example.billingservice.services.UserService;
import com.example.billingservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @Test
    public void testLogin_Success() throws Exception {
        String email = "test@example.com";
        String password = "password";

        Mockito.when(userService.authenticate(email, password)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("email", email)
                .param("password", password))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/stores/all"));
    }

    @Test
    public void testLogin_UserNotFound() throws Exception {
        String email = "nonexistent@example.com";
        String password = "password";

        Mockito.when(userService.authenticate(email, password)).thenReturn(false);
        Mockito.when(userService.findByEmail(email)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("email", email)
                .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", "User does not exist."));
    }

    @Test
    public void testLogin_InvalidPassword() throws Exception {
        String email = "test@example.com";
        String password = "wrongpassword";

        Mockito.when(userService.authenticate(email, password)).thenReturn(false);
        Mockito.when(userService.findByEmail(email)).thenReturn(new User(email, "correctpassword")); // 假设User有一个带email和password的构造函数

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("email", email)
                .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", "Invalid password."));
    }

    @Test
    public void testEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/endpoint"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("endpoint"));
    }
}
