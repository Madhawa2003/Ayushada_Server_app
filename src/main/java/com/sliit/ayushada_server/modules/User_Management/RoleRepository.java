package com.sliit.ayushada_server.modules.User_Management;

import com.sliit.ayushada_server.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
