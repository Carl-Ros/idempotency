package org.carlRos.idempotency.model.entities;

import java.time.Instant;

public record OrderEntity(int id, int customerId, Instant orderTimestamp) {
}
