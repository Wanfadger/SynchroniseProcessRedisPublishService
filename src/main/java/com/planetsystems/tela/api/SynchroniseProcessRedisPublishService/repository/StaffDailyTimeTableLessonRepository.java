package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.StaffDailyTimeTable;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.StaffDailyTimeTableLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffDailyTimeTableLessonRepository extends JpaRepository<StaffDailyTimeTableLesson, String> {
    @Query("""
            SELECT SDTL FROM StaffDailyTimeTableLesson  AS SDTL
            JOIN FETCH SDTL.schoolClass AS SC
            JOIN FETCH SDTL.subject AS SUB
            WHERE SDTL.status <> 8 AND SC.status <> 8 AND SUB.status <> 8 
            
            AND SDTL.staffDailyTimeTable IN :staffDailyTimeTables
            """)
    List<StaffDailyTimeTableLesson> allIn(List<StaffDailyTimeTable> staffDailyTimeTables);
}
