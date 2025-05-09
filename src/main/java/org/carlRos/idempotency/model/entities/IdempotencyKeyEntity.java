package org.carlRos.idempotency.model.entities;

import java.time.Instant;
import java.util.UUID;

public record IdempotencyKeyEntity(UUID uuid, Instant CreatedAt) { }
