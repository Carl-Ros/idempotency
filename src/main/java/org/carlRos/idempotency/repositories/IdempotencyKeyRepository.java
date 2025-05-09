package org.carlRos.idempotency.repositories;

import org.carlRos.idempotency.model.entities.IdempotencyKeyEntity;
import org.carlRos.idempotency.model.entities.OrderEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IdempotencyKeyRepository {
    private final JdbcClient jdbcClient;

    public IdempotencyKeyRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void createIdempotencyKey(UUID uuid) {
        var sql = "INSERT INTO IDEMPOTENCYKEYS (uuid) VALUES (:uuid)";

        jdbcClient.sql(sql)
                .param("uuid", uuid)
                .update();
    }

    public Optional<IdempotencyKeyEntity> getIdempotencyKey(UUID uuid) {
        var sql = "SELECT uuid, createdAt FROM IDEMPOTENCYKEYS WHERE uuid = :uuid";

        return jdbcClient.sql(sql)
                .param("uuid", uuid)
                .query(this::idempotencyKeyRowMapper)
                .optional();
    }

    private IdempotencyKeyEntity idempotencyKeyRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new IdempotencyKeyEntity(
                UUID.fromString(rs.getString("uuid")),
                rs.getTimestamp("createdAt").toInstant());
    }

}
