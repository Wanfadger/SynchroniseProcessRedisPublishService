package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.LessonDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name="TimeTableLessons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableLesson extends ParentEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LessonDay lessonDay;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolClass_id")
    private SchoolClass schoolClass;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Subject subject;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer duration;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;

    private LocalTime classStartTime;
    private LocalTime classEndTime;


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private SchoolStaff schoolStaff;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "timeTable_id")
    private TimeTable  timeTable;

}
