package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.SchoolDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.SynchronizeSchoolDataDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.SystemAppFeedBack;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.AcademicTerm;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.School;
import org.springframework.http.ResponseEntity;

public interface SynchronizeMobileDataService {
    ResponseEntity<SystemAppFeedBack<Boolean>> synchronizeSchoolData(SynchronizeSchoolDataDTO dto);
    void publishSchoolData(SynchronizeSchoolDataDTO dto);
    void publishSchoolClasses(SchoolDTO schoolDTO) throws JsonProcessingException;
    void publishSchoolStaffs(SchoolDTO schoolDTO) throws JsonProcessingException;
    void publishSchoolClockIns(SchoolDTO schoolDTO, String dateParam) throws JsonProcessingException;
    void publishSchoolClockOuts(SchoolDTO schoolDTO , String dateParam) throws JsonProcessingException;
    void publishSubjects(SchoolDTO schoolDTO) throws JsonProcessingException;
    void publishLearnerEnrollments(SchoolDTO schoolDTO) throws JsonProcessingException;
    void publishLearnerAttendance(SchoolDTO schoolDTO,  String dateParam) throws JsonProcessingException;
    void publishStaffDailyTimeAttendanceSupervision(SchoolDTO schoolDTO, String dateParam) throws JsonProcessingException;
    void publishStaffDailyTimetableTaskSupervision(SchoolDTO schoolDTO, String dateParam) throws JsonProcessingException;


    void publishStaffDailyTimetables(SchoolDTO schoolDTO , String dateParam) throws JsonProcessingException;
    void publishDistricts(SchoolDTO schoolDTO) throws JsonProcessingException;
    void publishSchoolTimetables(SchoolDTO schoolDTO) throws JsonProcessingException;


}
