package com.sliit.ayushada_server.modules.User_Management;

import com.sliit.ayushada_server.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private User user;
    private String token;
}