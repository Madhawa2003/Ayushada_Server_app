package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments;

import com.sliit.ayushada_server.Entity.*;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Dto.PaymentRequest;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Dto.ReceiptResponse;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Exeption.InvalidBillingException;

import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository.CustomerOrderRepository;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository.InvoiceRepository;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository.PayTypeRepository;
import com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository.PaymentRepository;

import com.sliit.ayushada_server.modules.User_Management.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillingService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PayTypeRepository payTypeRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Transactional
    public ReceiptResponse processPayment(PaymentRequest req) {
        Invoice invoice = null;

        if (req.getInvoiceId() != null) {
            invoice = invoiceRepository.findById(req.getInvoiceId()).orElse(null);
        }

        // Create Order and Invoice if direct checkout from cart
        if (invoice == null) {
            String customerUserId = (req.getCustomerId() != null && !req.getCustomerId().isEmpty())
                    ? req.getCustomerId()
                    : "USR-1006";

            User customer = userRepository.findById(customerUserId)
                    .orElseGet(() -> {
                        List<User> list = userRepository.findAll();
                        if (!list.isEmpty()) {
                            return list.get(0);
                        }
                        // Create customer record if database is empty
                        User fallback = new User();
                        fallback.setUserId(customerUserId);
                        fallback.setFullName(req.getCustomerName() != null ? req.getCustomerName() : "Kamal Bandara");
                        fallback.setEmail("customer_" + System.currentTimeMillis() + "@aushadha.lk");
                        fallback.setPassword("no_pass");
                        fallback.setPhoneNo(req.getPhoneNo() != null ? req.getPhoneNo() : "0771234567");
                        fallback.setAddress(req.getShippingAddress() != null ? req.getShippingAddress() : "Colombo, Sri Lanka");
                        fallback.setStatus("ACTIVE");
                        return userRepository.save(fallback);
                    });

            CustomerOrder order = new CustomerOrder();
            order.setOrderNumber("ORD-2026-" + (1000 + (long)(Math.random() * 9000)));
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("Processing");
            order.setShippingAddress(req.getShippingAddress() != null ? req.getShippingAddress() : "No 45, Temple Road, Colombo 03");
            order.setDeliveryFee(new BigDecimal("250.00"));
            order.setCustomer(customer);

            List<OrderItem> orderItems = new ArrayList<>();
            if (req.getItems() != null) {
                for (PaymentRequest.CheckoutItemDTO it : req.getItems()) {
                    OrderItem item = new OrderItem();
                    item.setQuantity(it.getQuantity());
                    item.setUnitPrice(it.getUnitPrice());
                    item.setCustomerOrder(order);

                    if (it.getMedicineId() != null) {
                        Medicine med = new Medicine();
                        med.setMedicineId(it.getMedicineId());
                        item.setMedicine(med);
                    }
                    orderItems.add(item);
                }
            }
            order.setItems(orderItems);

            CustomerOrder savedOrder = customerOrderRepository.save(order);

            invoice = new Invoice();
            invoice.setInvoiceNumber("INV-2026-" + (1000 + (long)(Math.random() * 9000)));
            invoice.setDate(LocalDateTime.now());
            BigDecimal itemsSubtotal = req.getAmount().subtract(savedOrder.getDeliveryFee());
            invoice.setTotal(itemsSubtotal.compareTo(BigDecimal.ZERO) > 0 ? itemsSubtotal : req.getAmount());
            invoice.setDeliveryFee(savedOrder.getDeliveryFee());
            invoice.setNetTotal(req.getAmount());
            invoice.setStatus("PAID");
            invoice.setCustomerOrder(savedOrder);

            invoice = invoiceRepository.save(invoice);
        }

        // Retrieve PayType or create fallback
        Long targetPayTypeId = req.getPayTypeId() != null ? req.getPayTypeId() : 1L;
        PayType payType = payTypeRepository.findById(targetPayTypeId)
                .orElseGet(() -> {
                    PayType fallback = new PayType();
                    fallback.setName(targetPayTypeId == 3 ? "Cash on Delivery" : (targetPayTypeId == 2 ? "Bank Transfer" : "Credit Card"));
                    fallback.setDescription("Payment gateway method");
                    return payTypeRepository.save(fallback);
                });

        String paymentRef = "PAY-TXN-" + System.currentTimeMillis();

        Payment payment = new Payment();
        payment.setPaymentReference(paymentRef);
        payment.setDate(LocalDateTime.now());
        payment.setAmount(req.getAmount());
        payment.setStatus("SUCCESS");
        payment.setInvoice(invoice);
        payment.setPayType(payType);

        paymentRepository.save(payment);

        List<ReceiptResponse.ReceiptItemDTO> receiptItems = new ArrayList<>();
        if (req.getItems() != null && !req.getItems().isEmpty()) {
            for (PaymentRequest.CheckoutItemDTO it : req.getItems()) {
                receiptItems.add(ReceiptResponse.ReceiptItemDTO.builder()
                        .medicineName(it.getMedicineName())
                        .quantity(it.getQuantity())
                        .unitPrice(it.getUnitPrice())
                        .total(it.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                        .build());
            }
        }

        return ReceiptResponse.builder()
                .receiptReference(paymentRef)
                .invoiceNumber(invoice.getInvoiceNumber())
                .orderNumber(invoice.getCustomerOrder() != null ? invoice.getCustomerOrder().getOrderNumber() : "ORD-DIRECT")
                .paymentDate(payment.getDate())
                .paymentMethod(payType.getName())
                .customerName(req.getCustomerName() != null ? req.getCustomerName() : "Kamal Bandara")
                .shippingAddress(req.getShippingAddress() != null ? req.getShippingAddress() : "Colombo, Sri Lanka")
                .phoneNo(req.getPhoneNo() != null ? req.getPhoneNo() : "0771234567")
                .items(receiptItems)
                .subtotal(invoice.getTotal())
                .deliveryFee(invoice.getDeliveryFee())
                .netTotal(invoice.getNetTotal())
                .status("COMPLETED & PAID")
                .build();
    }

    public Invoice processRefund(Long id, String reason) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvalidBillingException("Invoice not found: " + id));

        invoice.setStatus("REFUNDED");
        invoice.setNetTerm("REFUNDED: " + reason);
        return invoiceRepository.save(invoice);
    }

    @Transactional // <-- MUST be present here
    public void deleteInvoice(Long id) {
        // 1. Delete child payment records first
        paymentRepository.deleteByInvoice_InvoiceId(id);

        // 2. Delete parent invoice
        invoiceRepository.deleteById(id);
    }
}