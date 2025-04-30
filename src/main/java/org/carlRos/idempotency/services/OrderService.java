package org.carlRos.idempotency.services;

import org.carlRos.idempotency.model.domain.Order;
import org.carlRos.idempotency.repositories.CustomerRepository;
import org.carlRos.idempotency.repositories.OrderRepository;
import org.carlRos.idempotency.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public Order getOrderById(int orderId) {
        var products = productRepository.getOrderedProducts(orderId);
        var orderEntity = orderRepository.getOrderById(orderId);
        var customer = customerRepository.getCustomerById(orderEntity.customerId());

        return new Order(products, customer, orderEntity.orderTimestamp());
    }
}
