package com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management;

import com.sliit.ayushada_server.Entity.CustomerOrder;
import com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Dto.OrderStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order_management")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderManagementController {

    @Autowired
    private OrderManagementService orderService;

    @PostMapping("/checkout")
    public CustomerOrder placeOrder(@RequestBody CustomerOrder order) {
        return orderService.placeOrder(order);
    }

    @GetMapping("/orders")
    public List<CustomerOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/customer/{customerId}")
    public List<CustomerOrder> getCustomerOrders(@PathVariable String customerId) {
        return orderService.getCustomerOrders(customerId);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }

    @PutMapping("/orders/{id}/status")
    public CustomerOrder updateStatus(@PathVariable Long id, @RequestBody OrderStatusRequest req) {
        return orderService.updateStatus(id, req);
    }
}