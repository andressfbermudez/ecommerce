package com.api.ecommerce.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.api.ecommerce.persistence.entity.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByIsActiveTrue();
    Optional<Vehicle> findByIdAndIsActiveTrue(Long id);
    boolean existsByIdAndIsActiveTrue(Long id);

    @Query(value = """
                SELECT * FROM vehicles
                WHERE (:brand IS NULL OR brand = :brand)
                AND (:model IS NULL OR model = :model)
                AND (:year IS NULL OR year = :year)
                AND (:price IS NULL OR price = :price)
                AND (:doors IS NULL OR doors = :doors)
                AND (:color IS NULL OR color = :color)
                AND (:location IS NULL OR location = :location)
                AND is_active = TRUE
                """, nativeQuery = true)
    List<Vehicle> search(String brand, String model, Integer year,
                         Double price, Integer doors, String color, String location);
}
