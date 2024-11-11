package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.AuthAccountType;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.ConfigRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="SystemUsers")
@Setter
@Getter
@NoArgsConstructor
public class SystemUser extends ParentEntity {
	private String userName;
	private String password;
	private Boolean enabled = true;
	private LocalDateTime passwordChangedAt;

	private ConfigRole configRole = ConfigRole.ROLE_USER;

	@OneToOne(cascade= CascadeType.ALL,fetch= FetchType.LAZY , mappedBy = "systemUser")
	private SystemUserProfile systemUserProfile;

	@OneToMany(mappedBy = "supervisor")
	private Set<TimeAttendanceSupervision> attendanceSupervisions = new HashSet<>();


	private AuthAccountType authAccountType = AuthAccountType.SUPPORT;

	public SystemUser(String id) {
		super(id);
	}

	public SystemUser(String username, String password) {
		this.userName = username;
		this.password = password;
	}

}
