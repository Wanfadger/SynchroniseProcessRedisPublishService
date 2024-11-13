package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.ClockedStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "ClockIns")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class ClockIn extends ParentEntity {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "academicTerm_id")
	private AcademicTerm academicTerm;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolStaff_id")
	private SchoolStaff schoolStaff;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School school;

	@OneToOne(mappedBy = "clockIn", fetch = FetchType.LAZY , targetEntity = ClockOut.class)
	private ClockOut clockOut;

	private LocalDate clockInDate;

	private LocalTime clockInTime;

	private String comment;
	private String latitude;
	private String longitude;
	private ClockedStatus clockedStatus;


	private String clockinType;
	private Integer displacement;
	public ClockIn(String id) {
		super(id);
	}


}
