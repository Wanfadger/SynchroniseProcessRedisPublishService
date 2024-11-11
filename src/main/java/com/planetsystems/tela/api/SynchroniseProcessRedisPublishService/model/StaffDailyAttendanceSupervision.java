package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 root staff daily task supervision
 supervisions persisted into StaffDailyAttendanceTaskSupervisions
 */

/**
 * to be renamed to StaffDailyTimeTableSupervisions to improve readability and meaning
 * recurring to StaffDailyTimeTableLessonSupervisions
 */
//@NamedEntityGraph(name = "staff-daily-attendance-supervision-graph" , attributeNodes = {
//		@NamedAttributeNode("supervisor"),
//		@NamedAttributeNode("schoolStaff")}
//)
@Entity
@Table(name="StaffDailyAttendanceSupervisions")
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StaffDailyAttendanceSupervision extends ParentEntity{

	//to removed
    @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private SchoolStaff supervisor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolStaff_id")
    private SchoolStaff schoolStaff;

    private String comment;

    private LocalDate supervisionDate;

    private LocalTime supervisionTime;
     
    private AttendanceStatus attendanceStatus;

    /**
     * Supervised tasks
     */
    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY , targetEntity = StaffDailyAttendanceTaskSupervision.class , mappedBy = "staffDailyAttendanceSupervision")
    private Set<StaffDailyAttendanceTaskSupervision> staffDailyAttendanceTaskSupervisions = new HashSet<>();

    public StaffDailyAttendanceSupervision(String id) {
        super(id);
    }
}
