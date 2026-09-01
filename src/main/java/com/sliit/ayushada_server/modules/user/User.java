package com.sliit.ayushada_server.modules.user;

import com.sliit.ayushada_server.Repository.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(name = "email", length = 60)
    private String email;

    @Lob
    @Column(name = "password")
    private String password;

    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Role_id", nullable = false)
    private Role role;


}