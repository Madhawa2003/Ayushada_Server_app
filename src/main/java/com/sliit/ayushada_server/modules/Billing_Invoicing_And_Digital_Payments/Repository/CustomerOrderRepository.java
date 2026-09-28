package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository;

import com.sliit.ayushada_server.Entity.CustomerOrder;
import com.sliit.ayushada_server.Entity.PayType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
}