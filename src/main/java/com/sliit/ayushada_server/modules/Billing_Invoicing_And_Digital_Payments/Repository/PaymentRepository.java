package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository;

import com.sliit.ayushada_server.Entity.Invoice;
import com.sliit.ayushada_server.Entity.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    void deleteByInvoice(Invoice invoice);

    @Modifying
    @Transactional
    @Query("DELETE FROM Payment p WHERE p.invoice.invoiceId = :id")
    void deleteByInvoice_InvoiceId(@Param("id") Long id);
}