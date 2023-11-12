package com.group1.backend.repositories;

import com.group1.backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <UserEntity, Integer> {
    Optional<UserEntity> findByName(String name);
    Boolean existsByName(String name);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    @Query(value = """
            select U.name
             from user U
             where U.name like '%TestUser%'
             order by length(U.name) desc, U.name desc\s
             limit 1
             """,
            nativeQuery = true)
        Collection<Object> findLastTestUser();

    void deleteByName(String userName);
}
