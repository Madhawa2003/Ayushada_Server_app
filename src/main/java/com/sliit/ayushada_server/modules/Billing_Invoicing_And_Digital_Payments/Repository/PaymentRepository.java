package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository;

import com.sliit.ayushada_server.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}