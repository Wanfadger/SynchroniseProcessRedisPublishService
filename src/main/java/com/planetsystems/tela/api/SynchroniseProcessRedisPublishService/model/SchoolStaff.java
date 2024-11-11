package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.StaffInServiceStatus;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.StaffType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


//@NamedEntityGraph(name = "staff-with-details-graph" , attributeNodes = {
//		@NamedAttributeNode("generalUserDetail"),
//		@NamedAttributeNode(value = "school" , subgraph = "district-sub-graph")} ,
//  subgraphs = {
//		@NamedSubgraph(name = "district-sub-graph" , attributeNodes = @NamedAttributeNode("district"))
//  }
//)






@Entity
@Table(name = "SchoolStaffs")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class SchoolStaff extends ParentEntity implements Serializable {
	private String staffCode;
	private StaffType staffType; // Role/position from mobile
	@ManyToOne(fetch = FetchType.LAZY)
	private School school;
	@OneToOne(fetch = FetchType.LAZY , targetEntity = GeneralUserDetail.class , cascade = {CascadeType.PERSIST})
	private GeneralUserDetail generalUserDetail;

//	@OneToOne(mappedBy = "schoolStaff" , cascade = CascadeType.ALL ,  fetch = FetchType.LAZY)
//	private LearnerAttendance learnerAttendance;

	private boolean registered; // payroll status
	private boolean teachingstaff; // staff type from mobile

	private String registrationNo;
	private String nationality;

	private StaffInServiceStatus staffInServiceStatus = StaffInServiceStatus.ACTIVE;

	private Boolean specialNeeds=false;

//	@OneToOne(mappedBy = "schoolStaff" ,fetch = FetchType.LAZY)
//	private LearnerEnrollment learnerEnrollment;

//	@OneToOne(mappedBy = "schoolStaff")
//	private ClockIn clockIn;

	@OneToMany(mappedBy = "schoolStaff")
	private Set<TimeAttendanceSupervision> attendanceSupervisions = new HashSet<>();

	@OneToMany()
//	@OneToMany(mappedBy = "supervisor")
	private Set<TimeAttendanceSupervision> supervisors = new HashSet<>();

	private int expectedHours=40; // expected number of hours per week
	private int expectedDays=5; // expected number of days per week

//	@OneToOne(mappedBy = "schoolStaff")
//	private StaffDailyAttendanceSupervision staffDailyAttendanceSupervisions;

	public SchoolStaff(String id) {
		super(id);
	}

}
