package com.sliit.ayushada_server.modules.User_Management;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phoneNo;
    private String address;
}