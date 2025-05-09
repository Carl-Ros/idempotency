package org.carlRos.idempotency.services;

import org.carlRos.idempotency.model.domain.Product;
import org.carlRos.idempotency.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getOrderedProducts(int orderId) {
        return productRepository.getOrderedProducts(orderId);
    }

    public boolean hasEnoughStock(Collection<Product> orderedProducts){
        var orderedProductQuantities = orderedProducts
                .stream()
                .collect(Collectors.toMap(
                        Product::id,
                        Product::quantity,
                        Integer::sum
                ));

        var productsInStock = productRepository.getProductsByIds(orderedProductQuantities.keySet());

        return productsInStock
                .stream()
                .allMatch(p -> p.quantity() >= orderedProductQuantities.get(p.id()));
    }

    @Transactional
    public void createOrderedProducts(Collection<Product> products, int orderId) {
        var mergedProducts = products
                .stream()
                .collect(Collectors.toMap(
                        Product::id,
                        p -> new Product(p.id(), p.name(), p.price(), p.quantity()),
                        (prev, curr) -> new Product(prev.id(), prev.name(), prev.price(), curr.quantity() + prev.quantity())
                ));

        productRepository.decreaseProductsInStock(mergedProducts.values());
        productRepository.createOrderedProducts(mergedProducts.values(), orderId);
    }
}
