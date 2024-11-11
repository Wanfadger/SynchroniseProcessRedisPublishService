package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name="TimeTables")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTable extends ParentEntity {

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School school;


	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "academicTerm_id")
	private AcademicTerm academicTerm;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = TimeTableLesson.class, mappedBy = "timeTable")
	private List<TimeTableLesson> timeTableLessons;

	public TimeTable(String id) {
		super(id);
	}



}
