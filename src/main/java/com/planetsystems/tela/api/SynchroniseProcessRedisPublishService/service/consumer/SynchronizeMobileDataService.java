package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.consumer;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.SynchronizeSchoolDataDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.AcademicTerm;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.School;
import org.springframework.http.ResponseEntity;

public interface SynchronizeMobileDataService {
    ResponseEntity<Boolean> synchronizeSchoolData(SynchronizeSchoolDataDTO dto);



    void publishSchoolData(School school , AcademicTerm academicTerm);
    void publishSchoolClasses(School school, AcademicTerm academicTerm);
    void publishSchoolStaffs(School school, AcademicTerm academicTerm);
    void publishSchoolClockIns(School school, AcademicTerm academicTerm , String dateParam);
    void publishSchoolClockOuts(School school, AcademicTerm academicTerm , String dateParam);
    void publishSubjects(School school, AcademicTerm academicTerm);
    void publishLearnerEnrollments(School school, AcademicTerm academicTerm);
    void publishLearnerAttendance(School school, AcademicTerm academicTerm , String dateParam);
    void publishStaffDailyTimeAttendanceSupervision(School school, AcademicTerm academicTerm , String dateParam);
    void publishStaffDailyTimetableTaskSupervision(School school, AcademicTerm academicTerm , String dateParam);

    void publishStaffDailyTimetables(School school, AcademicTerm academicTerm , String dateParam);
    void publishDistricts(School school);
    void publishSchoolTimetables(School school, AcademicTerm academicTerm);


}
