package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;


import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
@SuperBuilder
@ToString
public class ParentEntity implements Serializable {

	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid") 
	@Id
	private String id;

	@CreationTimestamp
	private LocalDateTime createdDateTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateTime;
	private Status status = Status.ACTIVE;

	public ParentEntity() {
	}

	public ParentEntity(String id) {
		this.id = id;
	}

	public ParentEntity(LocalDateTime createdDateTime, LocalDateTime updatedDateTime , Status status) {
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = updatedDateTime;
		this.status = status;
	}

	public String getId() {
		return id;
	}

}
