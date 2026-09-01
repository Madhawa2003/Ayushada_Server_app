package com.sliit.ayushada_server.Repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "document")
    private String document;

    @Lob
    @Column(name = "note")
    private String note;

    @Column(name = "upload_at")
    private Instant uploadAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "User_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Verify_id", nullable = false)
    private Approve verify;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Order_id", nullable = false)
    private Order order;


}