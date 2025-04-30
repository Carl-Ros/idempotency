package org.carlRos.idempotency.controllers;

import org.carlRos.idempotency.model.messages.CreateOrderRequest;
import org.carlRos.idempotency.model.domain.Order;
import org.carlRos.idempotency.services.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public void createOrder(@RequestBody CreateOrderRequest request) {
        // Union order.products on product.id
        // Check all order.products

        // Start Transaction

            // Create order in Orders

            // Create OrderedProducts

            // Subtract quantity

        // Commit
    }

    @GetMapping("{orderId}")
    public Order getOrderById(@PathVariable int orderId){
        return orderService.getOrderById(orderId);
    }
}
