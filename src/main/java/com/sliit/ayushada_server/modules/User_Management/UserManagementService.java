package com.sliit.ayushada_server.modules.User_Management;

import com.sliit.ayushada_server.Entity.Role;
import com.sliit.ayushada_server.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public User createUser(User user) {
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            user.setUserId("USR-" + (int)(Math.random() * 9000 + 1000));
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword("$2a$10$w6h1pP9uJp6w9L1wPzBf1eW5zVpTqF5Y7Z4N1X3B8Q0M2V7T6Q1W.");
        }
        if (user.getRole() != null && user.getRole().getRoleId() != null) {
            Role role = roleRepository.findById(user.getRole().getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + user.getRole().getRoleId()));
            user.setRole(role);
        }
        return userRepository.save(user);
    }

    public User updateUser(String userId, User updated) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        existing.setFullName(updated.getFullName());
        existing.setEmail(updated.getEmail());
        existing.setPhoneNo(updated.getPhoneNo());
        existing.setAddress(updated.getAddress());
        existing.setStatus(updated.getStatus());

        if (updated.getRole() != null && updated.getRole().getRoleId() != null) {
            Role role = roleRepository.findById(updated.getRole().getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + updated.getRole().getRoleId()));
            existing.setRole(role);
        }

        return userRepository.save(existing);
    }

    /**
     * Soft-deactivates the user to preserve order history and relational integrity.
     */
    public void deleteUser(String userId) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        existing.setStatus("INACTIVE");
        userRepository.save(existing);
    }
}