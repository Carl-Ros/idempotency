package org.carlRos.idempotency.controllers;

import org.carlRos.idempotency.model.messages.CreateOrderRequest;
import org.carlRos.idempotency.model.domain.Order;
import org.carlRos.idempotency.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody CreateOrderRequest request) {
        orderService.createOrder(request.order());
    }

    @GetMapping("{orderId}")
    public Order getOrderById(@PathVariable int orderId) {
        return orderService.getOrderById(orderId);
    }
}
