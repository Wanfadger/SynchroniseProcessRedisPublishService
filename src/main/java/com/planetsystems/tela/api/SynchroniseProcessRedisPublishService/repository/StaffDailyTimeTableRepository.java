package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.StaffDailyTimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffDailyTimeTableRepository extends JpaRepository<StaffDailyTimeTable, String> {

    @Query("""
            SELECT SDT FROM StaffDailyTimeTable  AS SDT
            JOIN FETCH SDT.schoolStaff AS ST
            WHERE SDT.status <> 8 AND SDT.academicTerm.id =:termId AND ST.school.id =:schoolId
            """)
    List<StaffDailyTimeTable> allByTerm_School(String termId, String schoolId);


    @Query("""
            SELECT SDT FROM StaffDailyTimeTable  AS SDT
            JOIN FETCH SDT.schoolStaff AS ST
            WHERE SDT.status <> 8 AND SDT.lessonDate =:localDate AND ST.school.id =:schoolId
            """)
    List<StaffDailyTimeTable> allByDate_School(LocalDate localDate, String schoolId);

}
