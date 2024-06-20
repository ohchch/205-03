    package com.example.billingservice.services;

    import com.example.billingservice.model.User;
    import com.example.billingservice.repositories.UserRepository;
    import com.example.billingservice.repositories.PermissionRepository;
    import com.example.billingservice.model.Permissions;
    import jakarta.transaction.Transactional;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;

    @Service
    public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final BCryptPasswordEncoder passwordEncoder;
        private final PermissionRepository permissionRepository;

        public UserServiceImpl(UserRepository userRepository,PermissionRepository permissionRepository, BCryptPasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.permissionRepository = permissionRepository;
        }

        @Override
        public boolean authenticate(String email, String password) {
            User user = userRepository.findByEmail(email);
            return user != null && passwordEncoder.matches(password, user.getPassword());
        }

        @Override
        public void saveUser(User user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Permissions userPermission = permissionRepository.findByPermissions("User"); // 假设有这样的方法来查询权限对象
        
            if (userPermission != null) {
                user.addPermission(userPermission);
            } else {
                throw new IllegalArgumentException("User permission not found");
            }
            userRepository.save(user);
        }

        @Override
        public User findByEmail(String email) {
            return userRepository.findByEmail(email);
        }

        @Transactional
        @Override
        public void deleteUserByEmail(String email) {
            // Find the user by email
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("User not found with email: " + email);
            }

            // Delete the user
            userRepository.deleteById(user.getId());
        }
    }