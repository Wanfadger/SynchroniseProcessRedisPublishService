package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.StaffDailyAttendanceSupervision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffDailyAttendanceSupervisionRepository extends JpaRepository<StaffDailyAttendanceSupervision, String> {


    @Query("""
            SELECT SAS FROM StaffDailyAttendanceSupervision AS SAS
            JOIN FETCH SAS.schoolStaff AS ST
            WHERE SAS.status <> 8 AND ST.status <> 8
            AND ST.school.id =:schoolId AND SAS.supervisionDate BETWEEN :termStartDate AND :termEndDate
            """)
    List<StaffDailyAttendanceSupervision> allByTermDates_School(LocalDate termStartDate , LocalDate termEndDate , String schoolId );

    @Query("""
            SELECT SAS FROM StaffDailyAttendanceSupervision AS SAS
            JOIN FETCH SAS.schoolStaff AS ST
            WHERE SAS.status <> 8 AND ST.status <> 8
            AND ST.school.id =:schoolId AND SAS.supervisionDate =:localDate
            """)
    List<StaffDailyAttendanceSupervision> allByDate_School(LocalDate localDate, String schoolId);
}
