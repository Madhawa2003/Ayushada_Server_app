package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionVerifyRequest {
    private String status; // Approved, Rejected
    private String doctorName;
    private String ayurvedicRegNo;
    private String pharmacistNote;
    private String rejectionReason;
}