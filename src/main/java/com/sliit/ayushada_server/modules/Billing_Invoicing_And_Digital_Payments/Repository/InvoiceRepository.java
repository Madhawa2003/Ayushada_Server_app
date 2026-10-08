package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository;

import com.sliit.ayushada_server.Entity.CustomerOrder;
import com.sliit.ayushada_server.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByCustomerOrder(CustomerOrder customerOrder);
}