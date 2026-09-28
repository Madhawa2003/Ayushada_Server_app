package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto;

<<<<<<< Updated upstream
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PrescriptionFulfillmentRequest {
    private String doctorName;
    private String ayurvedicRegNo;
    private String pharmacistNote;
    private List<ItemDto> items;

    @Getter
    @Setter
    public static class ItemDto {
        private Long medicineId;
        private String medicineName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
=======
public class PrescriptionFulfillmentRequest {
}
>>>>>>> Stashed changes
