package org.carlRos.idempotency.model.messages;

import org.carlRos.idempotency.model.domain.Order;

import java.util.UUID;

public record CreateOrderRequest(Order order, UUID idempotencyKey) { }
