package org.carlRos.idempotency.repositories;

import org.carlRos.idempotency.model.domain.Order;
import org.carlRos.idempotency.model.entities.OrderEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class OrderRepository {
    private final JdbcClient jdbcClient;

    public OrderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public OrderEntity getOrderById(int orderId) {
        final String sql = "SELECT id, customerid, ordertimestamp FROM ORDERS WHERE id = :orderId";

        return jdbcClient.sql(sql)
                .param("orderId", orderId)
                .query(this::orderRowMapper)
                .single();
    }

    public int createOrder(Order order) {
        final String sql = "INSERT INTO ORDERS (customerid, ordertimestamp) VALUES (:customerId, :orderTimestamp)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
            .param("customerId", order.customer().id())
            .param("orderTimestamp", order.timestamp())
            .update(keyHolder);

        return keyHolder.getKeyAs(Integer.class);
    }

    private OrderEntity orderRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new OrderEntity(
                rs.getInt("id"),
                rs.getInt("customerid"),
                rs.getTimestamp("ordertimestamp").toInstant());
    }
}
