package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.TimeTableLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeTableLessonRepository extends JpaRepository<TimeTableLesson, String> {

    @Query("""
            SELECT TL FROM TimeTableLesson AS TL 
            WHERE TL.status <> 8 AND TL.timeTable.id =:timetableId
            """)
    List<TimeTableLesson> allByTimetable(String timetableId);

    @Query("""
            SELECT TL FROM TimeTableLesson AS TL 
            JOIN FETCH TL.timeTable AS T
            WHERE TL.status <> 8 AND T.status <> 8 
            AND T.academicTerm.id =:termId AND T.school.id =:schoolId AND TL.schoolClass.id =:classId  
            """)
    List<TimeTableLesson> allByTerm_School_Class( String termId, String schoolId , String classId);
}
