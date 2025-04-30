package org.carlRos.idempotency.repositories;

import jakarta.annotation.Nullable;
import org.carlRos.idempotency.model.domain.Product;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepository {
    private final JdbcClient jdbcClient;

    public ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Product> getProducts() {
        String sql = "SELECT id, name, price, quantity FROM PRODUCTS";

        return jdbcClient.sql(sql)
                .query(this::productRowMapper)
                .list();
    }

    public List<Product> getOrderedProducts(@Nullable Integer orderId) {
        String sql = "SELECT o.id AS id, p.name AS name, o.price AS price, o.quantity AS quantity FROM ORDEREDPRODUCTS o INNER JOIN PRODUCTS p ON o.productid = p.id";

        if (orderId != null){
            sql += " WHERE orderId = :orderId";
        }

        return jdbcClient.sql(sql)
                .param("orderId", orderId)
                .query(this::productRowMapper)
                .list();
    }

    private Product productRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getInt("quantity"));
    }
}
