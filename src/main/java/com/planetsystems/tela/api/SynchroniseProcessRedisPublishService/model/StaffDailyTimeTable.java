package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * to be renamed to StaffDailyTimeTable to improve readability and meaning
 * recurring to StaffDailyTimeTableLessons
 */
//@NamedEntityGraph(name = "staff-daily-timetable-graph", attributeNodes = {
//		@NamedAttributeNode(value = "academicTerm"),
//		@NamedAttributeNode(value = "schoolStaff"),
//		//@NamedAttributeNode(value = "staffDailyTimeTableLessons")
//})
@Entity
@Table(name="StaffDailyTimeTables")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class StaffDailyTimeTable extends ParentEntity{
	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY)
    private AcademicTerm academicTerm;

    @OneToOne(fetch = FetchType.LAZY)
    private SchoolStaff schoolStaff;

    private String comment;

//    @Temporal(TemporalType.DATE)
    private LocalDate lessonDate;

    @OneToMany(cascade = {CascadeType.PERSIST} , fetch = FetchType.LAZY, mappedBy = "staffDailyTimeTable" , targetEntity = StaffDailyTimeTableLesson.class)
    private List<StaffDailyTimeTableLesson> staffDailyTimeTableLessons;

    public StaffDailyTimeTable(String id) {
        super(id);
    }

}
