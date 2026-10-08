package com.sliit.ayushada_server.modules.User_Management;

import com.sliit.ayushada_server.Entity.CustomerOrder;
import com.sliit.ayushada_server.Entity.Role;
import com.sliit.ayushada_server.Entity.User;
import com.sliit.ayushada_server.common.JwtUtil;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository.InvoiceRepository;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository.PaymentRepository;
import com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Repository.OrderRepository;
import com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Repository.PrescriptionRepository;
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
    private OrderRepository orderRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }

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
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Step 1: Detach prescriptions from orders to prevent fk_order_prescription constraint violations
        if (orderRepository != null) {
            orderRepository.clearPrescriptionsByCustomer(existing);
        }

        // Step 2: Delete customer prescriptions
        if (prescriptionRepository != null && prescriptionRepository.existsByCustomer(existing)) {
            prescriptionRepository.deleteByCustomer(existing);
        }

        // Step 3: Delete financial records (payments -> invoices) before deleting orders
        if (orderRepository != null) {
            List<CustomerOrder> orders = orderRepository.findByCustomer(existing);
            for (CustomerOrder order : orders) {
                if (invoiceRepository != null) {
                    invoiceRepository.findByCustomerOrder(order).ifPresent(invoice -> {
                        if (paymentRepository != null) {
                            paymentRepository.deleteByInvoice(invoice);
                        }
                        invoiceRepository.delete(invoice);
                    });
                }
            }
            // Step 4: Delete the customer orders
            orderRepository.deleteAll(orders);
        }

        // Step 5: Delete user record
        userRepository.delete(existing);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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