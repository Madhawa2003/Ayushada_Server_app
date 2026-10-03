package com.sliit.ayushada_server.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "access_type", nullable = false, length = 100)
    private String accessType = "PORTAL_CUSTOMER";

    public Role(String name, String accessType) {
        this.name = name;
        this.accessType = accessType;
    }
}