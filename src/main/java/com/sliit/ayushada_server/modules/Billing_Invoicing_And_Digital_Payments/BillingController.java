package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments;

import com.sliit.ayushada_server.Entity.Invoice;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Dto.PaymentRequest;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Dto.ReceiptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/billing_management")
@CrossOrigin(origins = "http://localhost:4200")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices() {
        return billingService.getAllInvoices();
    }

    @PostMapping("/payments")
    public ReceiptResponse processPayment(@RequestBody PaymentRequest req) {
        return billingService.processPayment(req);
    }
    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Long id) {
        billingService.deleteInvoice(id);
    }

    @PutMapping("/invoices/{id}/refund")
    public Invoice processRefund(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return billingService.processRefund(id, body.get("reason"));
    }
}