package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelaPublicHoliday extends ParentEntity {

	@Column(nullable = false)
	private String name;
	private String description;

	@Column(nullable = false)
	private LocalDate date;

}
