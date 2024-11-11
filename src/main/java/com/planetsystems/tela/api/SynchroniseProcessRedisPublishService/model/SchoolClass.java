package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "SchoolClasses")
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolClass extends ParentEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String code;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School school;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "academicTerm_id")
	private AcademicTerm academicTerm;

	@OneToMany(mappedBy = "schoolClass" , fetch = FetchType.LAZY)
	private Set<TimeTableLesson> timeTableLessons = new HashSet<>();

	private boolean hasStreams;
	private boolean classLevel;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SchoolClass parentSchoolClass;

//	@OneToOne(mappedBy = "schoolClass" , fetch = FetchType.LAZY)
//	private LearnerEnrollment learnerEnrollment;

//	@OneToOne(mappedBy = "schoolClass" , fetch = FetchType.LAZY)
//	private LearnerAttendance learnerAttendance;



	public SchoolClass(String id) {
		super(id);
	}
}
