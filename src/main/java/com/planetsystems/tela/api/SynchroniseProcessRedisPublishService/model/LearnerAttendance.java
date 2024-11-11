package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Entity(name = "LearnerAttendances")
@Table(name="LearnerAttendances",indexes = {@Index(columnList = "attendanceDate,girlsPresent,boysPresent,boysAbsent,comment")})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LearnerAttendance extends ParentEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolClass_id")
	private SchoolClass schoolClass;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "academicTerm_id")
	private AcademicTerm academicTerm;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolStaff_id")
	private SchoolStaff schoolStaff;

	private LocalDate attendanceDate;

	private long girlsPresent;
	private long boysPresent;
	private long boysAbsent;
	private long girlsAbsent;
	private String comment;

}