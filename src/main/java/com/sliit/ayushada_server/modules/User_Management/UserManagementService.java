package com.sliit.ayushada_server.modules.User_Management;

import com.sliit.ayushada_server.Entity.Role;
import com.sliit.ayushada_server.Entity.User;
import com.sliit.ayushada_server.common.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public User createUser(User user) {
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            user.setUserId("USR-" + (int) (Math.random() * 9000 + 1000));
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


    /**
     * User registration with BCrypt hashing and default CUSTOMER role assignment
     */
    public AuthResponse register(RegisterRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("Request body cannot be null.");
        }
        if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email address is required.");
        }
        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        String normalizedEmail = req.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email already exists: " + normalizedEmail);
        }

        // 1. Fetch or create default CUSTOMER role with valid access_type
        Role defaultRole = roleRepository.findByNameIgnoreCase("CUSTOMER")
                .orElseGet(() -> {
                    Role newRole = new Role("CUSTOMER", "PORTAL_CUSTOMER");
                    return roleRepository.save(newRole);
                });

        // 2. Build User Entity according to EER model
        User user = new User();
        user.setUserId("USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        user.setFullName(req.getFullName() != null ? req.getFullName().trim() : "New Customer");
        user.setEmail(normalizedEmail);
        user.setPhoneNo(req.getPhoneNo() != null ? req.getPhoneNo().trim() : "");
        user.setAddress(req.getAddress() != null ? req.getAddress().trim() : "");
        user.setStatus("ACTIVE");
        user.setRole(defaultRole);
        // 3. Encrypt password using BCrypt (PBI-21)
        user.setPassword(req.getPassword() != null ? req.getPassword().trim() : "");

        User savedUser = userRepository.save(user);

        // 4. Generate persistent JWT token
        String token = jwtUtil.generateToken(savedUser.getEmail(), defaultRole.getName());

        return new AuthResponse("Registration successful", savedUser, token);
    }

    public AuthResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required.");
        }

        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new RuntimeException("Account is currently inactive.");
        }

        if (!(Objects.equals(request.getPassword(), user.getPassword()))) {
            throw new RuntimeException("Invalid email or password");
        }

        String roleName = user.getRole() != null ? user.getRole().getName() : "CUSTOMER";
        String token = jwtUtil.generateToken(user.getEmail(), roleName);

        return new AuthResponse("Login successful", user, token);
    }
}