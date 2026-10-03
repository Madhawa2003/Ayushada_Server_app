package com.sliit.ayushada_server.modules.User_Management;

import com.sliit.ayushada_server.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>
{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
