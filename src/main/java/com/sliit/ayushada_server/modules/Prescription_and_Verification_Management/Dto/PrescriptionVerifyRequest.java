package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto;

<<<<<<< Updated upstream
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionVerifyRequest {
    private String status; // Approved, Rejected
=======
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
public class PrescriptionVerificationRequest {
    @NotBlank(message = "Staff verifier user ID is required")
    private String verifierUserId;

    @NotBlank(message = "Target status is required")
    private String status; // Approved, Rejected, Pending Verification

>>>>>>> Stashed changes
    private String doctorName;
    private String ayurvedicRegNo;
    private String pharmacistNote;
    private String rejectionReason;
}