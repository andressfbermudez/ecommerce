package com.api.ecommerce.persistence.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.ecommerce.persistence.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = """
           SELECT *
           FROM users
           WHERE username = :input OR email = :input
           """, nativeQuery = true)
    Optional<UserEntity> findByUsernameOrEmail(@Param("input") String input);

}
