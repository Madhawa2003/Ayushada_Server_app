package com.sliit.ayushada_server.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescription")
@Getter
@Setter
@NoArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @Column(name = "prescription_number", unique = true, length = 50)
    private String prescriptionNumber;

    @Column(name = "upload_at")
    private LocalDateTime uploadAt = LocalDateTime.now();

    @Lob
    @Column(name = "document_url", columnDefinition = "LONGTEXT", nullable = false)
    private String documentUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type", length = 50)
    private String fileType;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "status", length = 40)
    private String status = "Pending Verification";

    @Column(name = "doctor_name", length = 150)
    private String doctorName;

    @Column(name = "ayurvedic_reg_no", length = 100)
    private String ayurvedicRegNo;

    @Column(name = "pharmacist_note", columnDefinition = "TEXT")
    private String pharmacistNote;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee = new BigDecimal("250.00");

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "roles", "orders"})
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verified_by")
    @JsonIgnoreProperties({"password", "roles", "orders"})
    private User verifiedBy;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PrescriptionItem> items = new ArrayList<>();
}