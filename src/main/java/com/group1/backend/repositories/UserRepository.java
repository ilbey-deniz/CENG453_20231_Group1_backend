package com.group1.backend.repositories;

import com.group1.backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <UserEntity, Integer> {
    Optional<UserEntity> findByName(String name);
    Boolean existsByName(String name);
}
