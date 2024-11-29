package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.AcademicTerm;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicTermRepository extends JpaRepository<AcademicTerm, String> {

    @Query(value = """
   SELECT A FROM AcademicTerms AS A JOIN FETCH A.academicYear AS Y
   WHERE A.status <> 8 AND A.activationStatus =:activationStatus
""")
    Optional<AcademicTerm> activeAcademicTerm(Status activationStatus);
}
