package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="TimeAttendanceSupervisions")
public class TimeAttendanceSupervision extends ParentEntity{

   private LocalDate supervisionDate;

   private LocalTime supervisionTime;
   private String comment;

   private Status attendanceStatus;

   @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
   @JoinColumn(name = "school_id")
   private School school;

   @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
   @JoinColumn(name = "schoolStaff_id")
   private SchoolStaff schoolStaff;

   @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.LAZY)
   @JoinColumn(name = "supervisor_id")
   private SystemUser supervisor;

   public TimeAttendanceSupervision() {
   }

   public LocalDate getSupervisionDate() {
      return supervisionDate;
   }

   public void setSupervisionDate(LocalDate supervisionDate) {
      this.supervisionDate = supervisionDate;
   }

   public LocalTime getSupervisionTime() {
      return supervisionTime;
   }

   public void setSupervisionTime(LocalTime supervisionTime) {
      this.supervisionTime = supervisionTime;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public School getSchool() {
      return school;
   }

   public void setSchool(School school) {
      this.school = school;
   }

   public SchoolStaff getSchoolStaff() {
      return schoolStaff;
   }

   public void setSchoolStaff(SchoolStaff schoolStaff) {
      this.schoolStaff = schoolStaff;
   }

   public Status getAttendanceStatus() {
      return attendanceStatus;
   }

   public void setAttendanceStatus(Status attendanceStatus) {
      this.attendanceStatus = attendanceStatus;
   }

   public SystemUser getSupervisor() {
      return supervisor;
   }

   public void setSupervisor(SystemUser supervisor) {
      this.supervisor = supervisor;
   }
}
