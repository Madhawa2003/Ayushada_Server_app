package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto;

<<<<<<< Updated upstream
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class PrescriptionDTO {
    private Long prescriptionId;
    private String prescriptionNumber;
    private LocalDateTime uploadAt;

    @NotBlank(message = "Document URL/data is required")
    private String documentUrl;

    private String fileName;
    private String fileType;
    private String note;
    private String status;
    private String doctorName;
    private String ayurvedicRegNo;
    private String pharmacistNote;
    private String rejectionReason;
    private BigDecimal deliveryFee;

    @NotBlank(message = "Customer ID is required")
    private String customerId;
    private String customerName;
    private String customerPhone;

    private String verifiedById;
    private String verifiedByName;
    private LocalDateTime verifiedAt;
}
=======
public class PrescriptionDTO {
}
>>>>>>> Stashed changes
