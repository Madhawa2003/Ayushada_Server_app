package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository;

import com.sliit.ayushada_server.Entity.PayType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayTypeRepository extends JpaRepository<PayType, Long> {
}