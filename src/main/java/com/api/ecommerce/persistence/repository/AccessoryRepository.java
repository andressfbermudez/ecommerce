package com.api.ecommerce.persistence.repository;

import com.api.ecommerce.persistence.entity.accessories.AccessoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessoryRepository extends JpaRepository<AccessoryEntity, Long> {

    List<AccessoryEntity> findByIsActiveTrue();

    boolean existsByIdAndIsActiveTrue(Long id);
    Optional<AccessoryEntity> findByIdAndIsActiveTrue(Long id);

    @Query(value = """
        SELECT *
        FROM accessories
        WHERE (:name IS NULL OR LOWER(name) LIKE CONCAT('%', LOWER(:name), '%'))
          AND (:brand IS NULL OR LOWER(brand) LIKE CONCAT('%', LOWER(:brand), '%'))
          AND (:price IS NULL OR price <= :price)
          AND is_active = TRUE
        """, nativeQuery = true)
    List<AccessoryEntity> search(String name, String brand, Double price);
}
