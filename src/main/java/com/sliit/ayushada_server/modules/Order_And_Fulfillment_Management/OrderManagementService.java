package com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management;

import com.sliit.ayushada_server.Entity.CustomerOrder;
import com.sliit.ayushada_server.Entity.OrderItem;
import com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Dto.OrderStatusRequest;
import com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Exeption.InvalidOrderException;
import com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderManagementService {

    @Autowired
    private OrderRepository orderRepository;

    public CustomerOrder placeOrder(CustomerOrder order) {
        order.setStatus("Processing");
        order.setOrderDate(LocalDateTime.from(Instant.from(LocalDateTime.now())));
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            order.setOrderNumber("ORD-" + System.currentTimeMillis());
        }

        // Sets the parent relationship for each order item
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setCustomerOrder(order);
            }
        }
        return orderRepository.save(order);
    }

    public List<CustomerOrder> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    public List<CustomerOrder> getCustomerOrders(String customerId) {
        return orderRepository.findByCustomerUserIdOrderByOrderDateDesc(customerId);
    }

    public CustomerOrder updateStatus(Long id, OrderStatusRequest req) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new InvalidOrderException("Order not found with ID: " + id));

        order.setStatus(req.getStatus());
        if (req.getAssignedTo() != null) {
            order.setAssignedTo(req.getAssignedTo());
        }
        return orderRepository.save(order);
    }
}