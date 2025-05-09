package org.carlRos.idempotency.services;

import org.carlRos.idempotency.model.domain.Order;
import org.carlRos.idempotency.repositories.CustomerRepository;
import org.carlRos.idempotency.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CustomerRepository customerRepository;
    private final TransactionHandler transactionHandler;

    public OrderService(OrderRepository orderRepository, ProductService productService, CustomerRepository customerRepository, TransactionHandler transactionHandler) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.customerRepository = customerRepository;
        this.transactionHandler = transactionHandler;
    }

    public Order getOrderById(int orderId) {
        var products = productService.getOrderedProducts(orderId);
        var orderEntity = orderRepository.getOrderById(orderId);
        var customer = customerRepository.getCustomerById(orderEntity.customerId());

        return new Order(products, customer, orderEntity.orderTimestamp());
    }


    public void createOrder(Order order) {
        var productsAreInStock = productService.hasEnoughStock(order.products());

        if(!productsAreInStock) {
            throw new IllegalArgumentException("All products in the order must be in stock.");
        }

        transactionHandler.runInTransaction(() -> createOrderTransaction(order));
    }

    private void createOrderTransaction(Order order) {
        var orderId = orderRepository.createOrder(order);
        productService.createOrderedProducts(order.products(), orderId);
        throw new RuntimeException("Haha!");
    }
}
