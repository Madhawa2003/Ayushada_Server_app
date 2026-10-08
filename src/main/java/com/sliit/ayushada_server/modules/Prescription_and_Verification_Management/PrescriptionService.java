package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management;

import com.sliit.ayushada_server.Entity.*;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository.InvoiceRepository;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository.PayTypeRepository;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository.PaymentRepository;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Repository.MedicineRepository;
import com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Repository.OrderRepository;
import com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto.CustomerApproveAndPayRequest;
import com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto.PrescriptionFulfillmentRequest;
import com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto.PrescriptionUploadRequest;
import com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Repository.PrescriptionRepository;
import com.sliit.ayushada_server.modules.User_Management.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service

public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PayTypeRepository payTypeRepository;
    @Autowired
    private UserRepository userRepository;


    public Prescription uploadPrescription(Prescription rx) {
        rx.setStatus("Pending Verification");
        rx.setUploadAt(LocalDateTime.now());
        if (rx.getPrescriptionNumber() == null || rx.getPrescriptionNumber().trim().isEmpty()) {
            rx.setPrescriptionNumber("RX-2026-" + (int)(Math.random() * 90000 + 10000));
        }
        return prescriptionRepository.save(rx);
    }

    public List<Prescription> getQueue() {
        return prescriptionRepository.findAllByOrderByUploadAtDesc();
    }

    public List<Prescription> getCustomerPrescriptions(String userId) {
        return prescriptionRepository.findByCustomer_UserIdOrderByUploadAtDesc(userId);
    }

    // Step 2: Pharmacist populates items & forwards to customer for approval
    public Prescription sendForApproval(Long id, PrescriptionFulfillmentRequest req) {
        Prescription rx = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + id));

        rx.setDoctorName(req.getDoctorName());
        rx.setAyurvedicRegNo(req.getAyurvedicRegNo());
        rx.setPharmacistNote(req.getPharmacistNote());
        rx.setStatus("Awaiting Approval");
        rx.setVerifiedAt(LocalDateTime.now());

        rx.getItems().clear();
        if (req.getItems() != null) {
            for (PrescriptionFulfillmentRequest.ItemDto dto : req.getItems()) {
                Medicine med = medicineRepository.findById(dto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + dto.getMedicineId()));

                PrescriptionItem item = new PrescriptionItem();
                item.setMedicine(med);
                item.setQuantity(dto.getQuantity());
                item.setUnitPrice(dto.getUnitPrice() != null ? dto.getUnitPrice() : med.getPrice());
                item.setPrescription(rx);
                rx.getItems().add(item);
            }
        }
        return prescriptionRepository.save(rx);
    }

    public Prescription rejectPrescription(Long id, String reason) {
        Prescription rx = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + id));
        rx.setStatus("Rejected");
        rx.setRejectionReason(reason);
        rx.setVerifiedAt(LocalDateTime.now());
        return prescriptionRepository.save(rx);
    }

    // Step 3: Customer approves and submits payment -> creates CustomerOrder, Invoice, Payment
    public CustomerOrder approveAndPay(Long prescriptionId, CustomerApproveAndPayRequest req) {
        Prescription rx = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + prescriptionId));

        rx.setStatus("Paid");
        prescriptionRepository.save(rx);

        // 1. Order
        CustomerOrder order = new CustomerOrder();
        order.setOrderNumber("ORD-2026-" + (int)(Math.random() * 90000 + 10000));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Preparing");
        order.setShippingAddress(req.getShippingAddress());
        order.setDeliveryFee(rx.getDeliveryFee() != null ? rx.getDeliveryFee() : new BigDecimal("250.00"));
        order.setPrescription(rx);
        order.setCustomer(rx.getCustomer());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (PrescriptionItem pi : rx.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setQuantity(pi.getQuantity());
            oi.setUnitPrice(pi.getUnitPrice());
            oi.setMedicine(pi.getMedicine());
            oi.setCustomerOrder(order);
            orderItems.add(oi);

            subtotal = subtotal.add(pi.getUnitPrice().multiply(BigDecimal.valueOf(pi.getQuantity())));
        }
        order.setItems(orderItems);
        CustomerOrder savedOrder = orderRepository.save(order);

        // 2. Invoice
        BigDecimal netTotal = subtotal.add(order.getDeliveryFee());
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-2026-" + (int)(Math.random() * 90000 + 10000));
        invoice.setDate(LocalDateTime.now());
        invoice.setTotal(subtotal);
        invoice.setDeliveryFee(order.getDeliveryFee());
        invoice.setNetTotal(netTotal);
        invoice.setStatus("PAID");
        invoice.setCustomerOrder(savedOrder);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // 3. Payment
        PayType payType = payTypeRepository.findById(req.getPayTypeId() != null ? req.getPayTypeId() : 1L).orElse(null);
        Payment payment = new Payment();
        payment.setPaymentReference("PAY-" + System.currentTimeMillis());
        payment.setDate(LocalDateTime.now());
        payment.setAmount(req.getAmountPaid() != null ? req.getAmountPaid() : netTotal);
        payment.setStatus("SUCCESS");
        payment.setInvoice(savedInvoice);
        payment.setPayType(payType);
        paymentRepository.save(payment);

        return savedOrder;
    }

    // Step 4: Advance delivery pipeline
    public Prescription advanceFulfillmentStatus(Long prescriptionId, String nextStatus) {
        Prescription rx = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + prescriptionId));
        rx.setStatus(nextStatus);
        return prescriptionRepository.save(rx);
    }

    public Prescription handleCustomerUpload(PrescriptionUploadRequest dto) {
        Prescription rx = new Prescription();
        rx.setPrescriptionNumber("RX-" + LocalDateTime.now().getYear() + "-" + (int)(Math.random() * 90000 + 10000));
        rx.setUploadAt(LocalDateTime.now());
        rx.setFileName(dto.getFileName());
        rx.setFileType(dto.getFileType());
        rx.setDocumentUrl(dto.getFileBase64());
        rx.setNote(dto.getCustomerNote());
        rx.setStatus("Pending Verification");
        rx.setDeliveryFee(new BigDecimal("250.00"));

        String targetUserId = (dto.getUserId() != null && !dto.getUserId().trim().isEmpty())
                ? dto.getUserId() : "USR-1006";
        User customer = userRepository.findById(targetUserId).orElse(null);
        rx.setCustomer(customer);

        return prescriptionRepository.save(rx);
    }


    public Prescription updateStatus(Long id, String status) {
        Prescription rx = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + id));
        rx.setStatus(status);
        return prescriptionRepository.save(rx);
    }

    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
}