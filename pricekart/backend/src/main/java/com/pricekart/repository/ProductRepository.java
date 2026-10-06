package com.pricekart.repository;

import com.pricekart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Simple keyword search across name and description (case‑insensitive).
     */
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
