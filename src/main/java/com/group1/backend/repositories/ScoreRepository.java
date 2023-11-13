package com.group1.backend.repositories;

import com.group1.backend.entities.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreEntity, Integer> {


    @Query(
            value = """
                    select U.name, sum(S.score) as totalScore
                    from user U, score S
                    where U.id = S.user_id and S.time >= DATE_SUB(NOW(), INTERVAL 1 WEEK)
                    group by U.name
                    order by sum(S.score) desc""",
            nativeQuery = true)
    Collection<Object[]> findTopScorerWeekly();

    @Query(
            value = """
                    select U.name, sum(S.score) as totalScore
                    from user U, score S
                    where U.id = S.user_id and S.time >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
                    group by U.name
                    order by sum(S.score) desc""",
            nativeQuery = true)
    Collection<Object[]> findTopScorerMonthly();

    @Query(
            value = """
                    select U.name, sum(S.score) as totalScore
                    from user U, score S
                    where U.id = S.user_id
                    group by U.name
                    order by sum(S.score) desc""",
            nativeQuery = true)
    Collection<Object[]> findTopScorerAllTime();

    @Query(
            value = """
                    select sum(S.score) as total_score
                    from user U, score S
                    where U.id = S.user_id and U.name = :name
                    group by U.name""",
            nativeQuery = true)
    Collection<Object> findTotalScoreByName(@Param("name") String name);
}
