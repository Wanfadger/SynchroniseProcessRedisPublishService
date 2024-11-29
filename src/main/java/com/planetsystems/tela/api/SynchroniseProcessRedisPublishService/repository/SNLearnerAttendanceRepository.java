package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.SNLearnerAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SNLearnerAttendanceRepository extends JpaRepository<SNLearnerAttendance, String> {

    @Query(value = """
            SELECT LA FROM SNLearnerAttendance  AS LA
            JOIN FETCH LA.schoolClass AS SC
            WHERE LA.status <> 8 AND SC.status <> 8 
            AND LA.academicTerm.id =:termId AND SC.school.id =:schoolId
            """)
    List<SNLearnerAttendance> allByTerm_School(String termId, String schoolId);


    @Query(value = """
            SELECT LA FROM SNLearnerAttendance  AS LA
            JOIN FETCH LA.schoolClass AS SC
            WHERE LA.status <> 8 AND SC.status <> 8 
            AND LA.attendanceDate =:attendanceDate AND SC.school.id =:schoolId
            """)
    List<SNLearnerAttendance> allByDate_School(LocalDate attendanceDate, String schoolId);

}
