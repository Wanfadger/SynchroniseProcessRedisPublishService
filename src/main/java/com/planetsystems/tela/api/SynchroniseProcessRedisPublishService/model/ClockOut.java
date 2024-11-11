package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.ClockedStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "ClockOuts")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString
public class ClockOut extends ParentEntity {

	@OneToOne(fetch = FetchType.LAZY , targetEntity = ClockIn.class)
	@JoinColumn(name = "clockIn_id", unique = true)
	private ClockIn clockIn;

	private LocalDate clockOutDate;

	private LocalTime clockOutTime;

	private ClockedStatus clockedStatus;
    
    private String comment;
    // add enum to track clockinout type [PIN , FACE , FINGER , AUTO]

	private String clockOutType;

	private Integer displacement;


}
