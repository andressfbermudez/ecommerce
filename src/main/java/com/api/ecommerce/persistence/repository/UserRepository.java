package com.api.ecommerce.persistence.repository;

import org.springframework.stereotype.Repository;
import com.api.ecommerce.persistence.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
