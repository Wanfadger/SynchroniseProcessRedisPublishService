package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

//Learners with special needs head count
@Entity
@Table(name = "SNLearnerEnrollments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SNLearnerEnrollment extends ParentEntity{

	private static final long serialVersionUID = 1L;
	
	@OneToOne(fetch = FetchType.LAZY)
	private SchoolClass schoolClass;
	private long totalBoys;
	private long totalGirls;

	private Status enrollmentStatus;

	private LocalDate submissionDate;

	// Submitted by staff
	@OneToOne(fetch = FetchType.LAZY)
	private SchoolStaff schoolStaff;


}
