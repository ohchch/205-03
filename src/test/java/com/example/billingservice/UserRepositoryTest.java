package com.example.billingservice;

import com.example.billingservice.entities.User;
import com.example.billingservice.repositories.UserRepository;

//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    /*@BeforeEach
    public void setUp() {
        // 在每个测试方法运行之前初始化模拟对象
        User user = new User("test@example.com", "password");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
    }*/

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";

        // 调用存储库方法
        User foundUser = userRepository.findByEmail(email);

        // 断言找到的用户不为空
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
    }
}
