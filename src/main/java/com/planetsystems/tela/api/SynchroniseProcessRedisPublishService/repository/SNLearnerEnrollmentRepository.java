package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.SNLearnerEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SNLearnerEnrollmentRepository extends JpaRepository<SNLearnerEnrollment, String> {

    @Query(value = """
            SELECT LE FROM SNLearnerEnrollment AS LE
            JOIN FETCH LE.schoolClass AS SC
            WHERE LE.enrollmentStatus <> 8 AND SC.status <> 8
            AND SC.school.id =:schoolId AND SC.academicTerm.id =:termId
            """)
    List<SNLearnerEnrollment> allBySchool_term(String schoolId, String termId);
}
