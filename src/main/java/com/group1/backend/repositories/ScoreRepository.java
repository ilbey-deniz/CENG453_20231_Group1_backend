package com.group1.backend.repositories;

import com.group1.backend.entities.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreEntity, Integer> {


}
