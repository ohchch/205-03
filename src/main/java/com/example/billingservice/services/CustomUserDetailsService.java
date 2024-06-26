package com.example.billingservice.services;

import com.example.billingservice.entities.Permissions;
import com.example.billingservice.entities.User;
import com.example.billingservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Permissions permissions : user.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(permissions.getPermissions()));
        }

        // 返回带有用户ID的自定义UserDetails
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }
}
