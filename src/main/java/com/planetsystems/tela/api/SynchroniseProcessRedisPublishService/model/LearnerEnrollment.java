package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name="LearnerEnrollments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LearnerEnrollment extends ParentEntity{

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolClass_id")
    private SchoolClass schoolClass;
    private long totalBoys;
    private long totalGirls; 
    //Submitted by staff
    private Status enrollmentStatus;
	private LocalDate submissionDate;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolStaff_id")
	private SchoolStaff schoolStaff;


}
