package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;
import jakarta.persistence.*;

@Entity
@Table(name="TeacherLessons")
public class StaffLesson extends ParentEntity{
	
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private School school;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private AcademicTerm academicTerm;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private SchoolStaff schoolStaff;

    public StaffLesson() {
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public AcademicTerm getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(AcademicTerm academicTerm) {
        this.academicTerm = academicTerm;
    }

    public SchoolStaff getSchoolStaff() {
        return schoolStaff;
    }

    public void setSchoolStaff(SchoolStaff schoolStaff) {
        this.schoolStaff = schoolStaff;
    }
}
