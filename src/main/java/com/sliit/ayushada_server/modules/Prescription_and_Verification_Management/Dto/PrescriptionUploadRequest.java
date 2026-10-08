package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionUploadRequest {
    private String fileName;
    private String fileType;
    private String fileBase64;
    private String customerNote;
    private String userId;
}