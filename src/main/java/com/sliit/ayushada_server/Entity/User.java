package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_no", length = 20)
    private String phoneNo;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}