package com.api.ecommerce.persistence.repository;

import com.api.ecommerce.persistence.entity.accessories.AccessoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessoryRepository extends JpaRepository<AccessoryEntity, Long> {
}
