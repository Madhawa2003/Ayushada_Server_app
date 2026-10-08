// ComplianceLogDTO.java
package com.sliit.ayushada_server.modules.Reporting_And_Analytics.Dto;

import java.time.LocalDateTime;

public class ComplianceLogDTO {
    private String logId;
    private String prescriptionNumber;
    private String medicineName;
    private String category;
    private String patientName;
    private String practitionerRegNo;
    private LocalDateTime dispensedDate;
    private String complianceStatus;

    public ComplianceLogDTO(String logId, String prescriptionNumber, String medicineName,
                            String category, String patientName, String practitionerRegNo,
                            LocalDateTime dispensedDate, String complianceStatus) {
        this.logId = logId;
        this.prescriptionNumber = prescriptionNumber;
        this.medicineName = medicineName;
        this.category = category;
        this.patientName = patientName;
        this.practitionerRegNo = practitionerRegNo;
        this.dispensedDate = dispensedDate;
        this.complianceStatus = complianceStatus;
    }

    public String getLogId() { return logId; }
    public String getPrescriptionNumber() { return prescriptionNumber; }
    public String getMedicineName() { return medicineName; }
    public String getCategory() { return category; }
    public String getPatientName() { return patientName; }
    public String getPractitionerRegNo() { return practitionerRegNo; }
    public LocalDateTime getDispensedDate() { return dispensedDate; }
    public String getComplianceStatus() { return complianceStatus; }
}