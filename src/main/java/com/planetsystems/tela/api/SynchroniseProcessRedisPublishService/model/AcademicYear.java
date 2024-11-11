package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity(name = "AcademicYears")
@Table(name="AcademicYears",indexes = {@Index(columnList = "code,name")})
@Setter
@Getter
@NoArgsConstructor
public class AcademicYear extends ParentEntity implements Serializable {
	private String code;


	private String name;
	private Status activationStatus;

	@OneToMany(mappedBy = "academicYear" , orphanRemoval = true , fetch = FetchType.LAZY)
	private Set<AcademicTerm> academicTerms;

	private LocalDate startDate;
	private LocalDate endDate;

	public AcademicYear(String id) {
		super(id);
	}


}
