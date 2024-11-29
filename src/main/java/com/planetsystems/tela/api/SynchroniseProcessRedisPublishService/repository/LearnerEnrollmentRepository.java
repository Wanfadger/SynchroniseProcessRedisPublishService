package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.LearnerEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearnerEnrollmentRepository extends JpaRepository<LearnerEnrollment, String> {

    @Query(value = """
            SELECT LE FROM LearnerEnrollment AS LE
            JOIN FETCH LE.schoolClass AS SC
            WHERE LE.enrollmentStatus <> 8 AND SC.status <> 8
            AND SC.school.id =:schoolId AND SC.academicTerm.id =:termId
            """)
    List<LearnerEnrollment> allBySchool_term(String schoolId, String termId);

}
