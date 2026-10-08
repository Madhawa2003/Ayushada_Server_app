package com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Repository;

import com.sliit.ayushada_server.Entity.CustomerOrder;
import com.sliit.ayushada_server.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findAllByOrderByOrderDateDesc();
    List<CustomerOrder> findByCustomerUserIdOrderByOrderDateDesc(String customerId);
    boolean existsByCustomer(User user);
    void deleteByCustomer(User user);
    List<CustomerOrder> findByCustomer(User user);
    @Modifying
    @Query("UPDATE CustomerOrder o SET o.prescription = null WHERE o.customer = :customer")
    void clearPrescriptionsByCustomer(@Param("customer") User customer);
}