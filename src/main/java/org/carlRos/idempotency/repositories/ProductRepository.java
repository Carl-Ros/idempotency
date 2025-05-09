package org.carlRos.idempotency.repositories;

import jakarta.annotation.Nullable;
import org.carlRos.idempotency.model.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
public class ProductRepository {
    private final JdbcClient jdbcClient;
    private final JdbcTemplate jdbcTemplate; // Use for batch processing
    private final int DEFAULT_BATCH_SIZE = 100;

    public ProductRepository(JdbcClient jdbcClient, JdbcTemplate jdbcTemplate) {
        this.jdbcClient = jdbcClient;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getProducts() {
        var sql = "SELECT id, name, price, quantity FROM PRODUCTS";

        return jdbcClient.sql(sql)
                .query(this::productRowMapper)
                .list();
    }

    public List<Product> getProductsByIds(Collection<Integer> productIds) {
        var sql = "SELECT id, name, price, quantity FROM PRODUCTS WHERE id IN (:productIds)";

        return jdbcClient.sql(sql)
                .param("productIds", productIds)
                .query(this::productRowMapper)
                .list();
    }

    public List<Product> getOrderedProducts(@Nullable Integer orderId) {
        var sql = "SELECT p.id AS id, p.name AS name, o.price AS price, o.quantity AS quantity FROM ORDEREDPRODUCTS o INNER JOIN PRODUCTS p ON o.productid = p.id";

        if (orderId != null){
            sql += " WHERE orderId = :orderId";
        }

        return jdbcClient.sql(sql)
                .param("orderId", orderId)
                .query(this::productRowMapper)
                .list();
    }


    public void createOrderedProducts(Collection<Product> products, int orderId) {
        jdbcTemplate.batchUpdate("INSERT INTO ORDEREDPRODUCTS (productid, orderid, price, quantity) VALUES (?, ?, ?, ?)",
            products,
            DEFAULT_BATCH_SIZE,
            (PreparedStatement ps, Product p) -> {
                ps.setInt(1, p.id());
                ps.setInt(2, orderId);
                ps.setInt(3, p.price());
                ps.setInt(4, p.quantity());
            });
    }

    public void decreaseProductsInStock(Collection<Product> products) {
        jdbcTemplate.batchUpdate("UPDATE PRODUCTS SET quantity = (quantity - ?) WHERE id = ?",
            products,
            DEFAULT_BATCH_SIZE,
            (PreparedStatement ps, Product p) -> {
                ps.setInt(1, p.quantity());
                ps.setInt(2, p.id());
            });
    }

    private Product productRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getInt("quantity"));
    }
}
