package org.carlRos.idempotency.model.messages;

import org.carlRos.idempotency.model.domain.Order;

public record CreateOrderRequest(Order order) { }
