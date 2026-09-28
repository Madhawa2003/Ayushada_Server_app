package com.sliit.ayushada_server.modules.User_Management;

import com.sliit.ayushada_server.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>
{
}
