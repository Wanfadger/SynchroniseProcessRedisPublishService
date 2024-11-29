package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.TimeTable;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository.projections.IdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, String> {
    Optional<IdProjection> findBySchool_IdAndAcademicTerm_Id(String schoolId, String termId);
}
