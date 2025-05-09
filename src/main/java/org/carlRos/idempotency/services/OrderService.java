package org.carlRos.idempotency.services;

import org.carlRos.idempotency.model.domain.Order;
import org.carlRos.idempotency.model.messages.CreateOrderRequest;
import org.carlRos.idempotency.repositories.CustomerRepository;
import org.carlRos.idempotency.repositories.IdempotencyKeyRepository;
import org.carlRos.idempotency.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CustomerRepository customerRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final TransactionHandler transactionHandler;

    public OrderService(OrderRepository orderRepository, ProductService productService, CustomerRepository customerRepository, TransactionHandler transactionHandler, IdempotencyKeyRepository idempotencyKeyRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.customerRepository = customerRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.transactionHandler = transactionHandler;

    }

    public Order getOrderById(int orderId) {
        var products = productService.getOrderedProducts(orderId);
        var orderEntity = orderRepository.getOrderById(orderId);
        var customer = customerRepository.getCustomerById(orderEntity.customerId());

        return new Order(products, customer, orderEntity.orderTimestamp());
    }

    public void createOrder(CreateOrderRequest request) {
        var idempotencyKey = idempotencyKeyRepository.getIdempotencyKey(request.idempotencyKey());
        if (idempotencyKey.isPresent()) {
            return;
        }

        var productsAreInStock = productService.hasEnoughStock(request.order().products());

        if(!productsAreInStock) {
            throw new IllegalArgumentException("All products in the order must be in stock.");
        }

        transactionHandler.runInTransaction(() -> createOrderTransaction(request));
    }

    private void createOrderTransaction(CreateOrderRequest request) {
        idempotencyKeyRepository.createIdempotencyKey(request.idempotencyKey());
        var orderId = orderRepository.createOrder(request.order());
        productService.createOrderedProducts(request.order().products(), orderId);
    }
}
