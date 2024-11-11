package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Staff daily attendance lessons
 */
//@NamedEntityGraph(name = "staff-daily-timeTable-lesson-detail-graph" , attributeNodes = {
//		@NamedAttributeNode(value = "schoolClass"),
//		@NamedAttributeNode(value = "subject"),
//		@NamedAttributeNode(value = "staffDailyTimeTable")
//}
//)
@Entity
@Table(name="StaffDailyTimeTableLessons")
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StaffDailyTimeTableLesson extends ParentEntity{

    /**
	 * 
	 */ 
	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY)
    private SchoolClass schoolClass;

    @OneToOne(fetch = FetchType.LAZY)
    private Subject subject;

//    @Temporal(TemporalType.DATE)
    private LocalDate lessonDate;

    private LocalTime startTime;
    private LocalTime endTime;
    private AttendanceStatus dailyTimeTableLessonStatus;

    @ManyToOne(cascade = {CascadeType.REFRESH , CascadeType.PERSIST} , fetch = FetchType.LAZY)
    private StaffDailyTimeTable staffDailyTimeTable;

    public StaffDailyTimeTableLesson(String id) {
        super(id);
    }

}
