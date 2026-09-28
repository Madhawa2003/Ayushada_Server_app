package com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Repository;

import com.sliit.ayushada_server.Entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findAllByOrderByOrderDateDesc();
    List<CustomerOrder> findByCustomerUserIdOrderByOrderDateDesc(String customerId);
}