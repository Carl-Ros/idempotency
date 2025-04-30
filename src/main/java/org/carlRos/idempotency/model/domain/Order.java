package org.carlRos.idempotency.model.domain;

import java.time.Instant;
import java.util.List;

public record Order (List<Product> products, Customer customer, Instant timestamp) {

    public Order(List<Product> products, Customer customer, Instant timestamp) {
        this.products = products;
        this.customer = customer;

        if(timestamp.isAfter(Instant.now())) {
            throw new IllegalArgumentException("Order date cannot be in the future.");
        }
        this.timestamp = timestamp;
    }
}

