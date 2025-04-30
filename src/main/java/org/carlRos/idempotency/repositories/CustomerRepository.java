package org.carlRos.idempotency.repositories;

import org.carlRos.idempotency.model.domain.Customer;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CustomerRepository {
    private final JdbcClient jdbcClient;

    public CustomerRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Customer getCustomerById(int customerId) {
        final String sql = "SELECT id, email FROM CUSTOMERS WHERE id = :customerId";

        return jdbcClient.sql(sql)
                .param("customerId", customerId)
                .query(this::customerRowMapper)
                .single();
    }

    private Customer customerRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(
                rs.getInt("id"),
                rs.getString("email"));
    }
}

