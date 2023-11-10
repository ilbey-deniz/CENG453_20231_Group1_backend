package com.group1.backend.repositories;

import com.group1.backend.dto.TopScoreUserDto;
import com.group1.backend.entities.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreEntity, Integer> {
    @Query(
            value = "select U.name, sum(S.score) as totalScore\n" +
                    "from user U, score S\n" +
                    "where U.id = S.user_id and S.time >= DATE_SUB(NOW(), INTERVAL 1 WEEK)\n" +
                    "group by U.name\n" +
                    "order by sum(S.score) desc",
            nativeQuery = true)
    Collection<Object[]> findTopScorerDescending();

}
