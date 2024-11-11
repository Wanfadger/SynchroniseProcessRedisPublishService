package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
//@Table(name = "SNLearnerAttendances", indexes = {
//		@Index(columnList = "id,attendanceDate,girlsPresent,boysPresent,boysAbsent,comment") })
//@Cache(region = "snLearnerAttendanceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "SNLearnerAttendances")
@NamedEntityGraph(name = "snlearnerattendance-schoolclass-academicterm-staff-graph", attributeNodes = {
		@NamedAttributeNode(value = "schoolClass"),
		@NamedAttributeNode(value = "academicTerm"),
		@NamedAttributeNode(value = "schoolStaff") })
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SNLearnerAttendance extends ParentEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5660869902917768992L;

	@OneToOne(fetch = FetchType.LAZY)
	private SchoolClass schoolClass;

	@OneToOne(fetch = FetchType.LAZY)
	private AcademicTerm academicTerm;

	@OneToOne(fetch = FetchType.LAZY)
	private SchoolStaff schoolStaff;

	private LocalDate attendanceDate;

	private long girlsPresent;
	private long boysPresent;
	private long boysAbsent;
	private long girlsAbsent;
	private String comment;

}
