package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management;

import com.sliit.ayushada_server.Entity.CustomerOrder;
import com.sliit.ayushada_server.Entity.Prescription;
import com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto.CustomerApproveAndPayRequest;
import com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto.PrescriptionFulfillmentRequest;
import com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto.PrescriptionUploadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prescription_management")
@CrossOrigin(origins = "http://localhost:4200")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/upload")
    public Prescription uploadPrescription(@RequestBody Prescription rx) {
        return prescriptionService.uploadPrescription(rx);
    }

    @PostMapping("/customer/upload")
    public Prescription uploadFromCustomer(@RequestBody PrescriptionUploadRequest request) {
        return prescriptionService.handleCustomerUpload(request);
    }

//    @GetMapping("/customer/{userId}")
//    public List<Prescription> getCustomerHistory(@PathVariable String userId) {
//        return prescriptionService.getCustomerPrescriptions(userId);
//    }

    @PatchMapping("/{id}/status")
    public Prescription changeStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return prescriptionService.updateStatus(id, body.get("status"));
    }

    @GetMapping("/queue")
    public List<Prescription> getQueue() {
        return prescriptionService.getQueue();
    }

    @GetMapping("/customer/{userId}")
    public List<Prescription> getCustomerPrescriptions(@PathVariable String userId) {
        return prescriptionService.getCustomerPrescriptions(userId);
    }

    @PutMapping("/verify/{id}/send-approval")
    public Prescription sendForApproval(
            @PathVariable Long id,
            @RequestBody PrescriptionFulfillmentRequest req) {
        return prescriptionService.sendForApproval(id, req);
    }

    @PutMapping("/verify/{id}/reject")
    public Prescription rejectPrescription(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return prescriptionService.rejectPrescription(id, body.get("reason"));
    }

    @PostMapping("/customer/{id}/approve-and-pay")
    public CustomerOrder approveAndPay(
            @PathVariable Long id,
            @RequestBody CustomerApproveAndPayRequest req) {
        return prescriptionService.approveAndPay(id, req);
    }

    @PutMapping("/{id}/fulfillment-status")
    public Prescription advanceFulfillmentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return prescriptionService.advanceFulfillmentStatus(id, body.get("status"));
    }
}

