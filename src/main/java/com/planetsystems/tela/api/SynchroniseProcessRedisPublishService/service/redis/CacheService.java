package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.redis;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.*;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.supervision.StaffDailyAttendanceTaskSupervisionDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.StaffDailyTimetableDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.TimetableDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.AcademicTerm;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.School;

import java.util.List;

public interface CacheService {

    AcademicTermDTO cacheActiveAcademicTerm();
    SchoolDTO cacheSchoolData(String telaSchoolNumber);
    MQResponseDto<List<ClassDTO>> cacheSchoolClasses(SchoolDTO schoolDTO);
    MQResponseDto<List<StaffDTO>> cacheSchoolStaffs(SchoolDTO schoolDTO);
    MQResponseDto<List<ClockInDTO>> cacheSchoolTermClockIns(SchoolDTO schoolDTO);
    MQResponseDto<List<ClockOutDTO>> cacheSchoolTermClockOuts(SchoolDTO schoolDTO);
    MQResponseDto<List<IdNameCodeDTO>> cacheSubjects(SchoolDTO schoolDT);
    MQResponseDto<List<LearnerHeadCountDTO>> cacheLearnerEnrollments(SchoolDTO schoolDTO);
    MQResponseDto<List<LearnerAttendanceDTO>> cacheLearnerAttendance(SchoolDTO schoolDTO);
    MQResponseDto<List<StaffDailyTimeAttendanceDTO>> cacheStaffDailyTimeAttendanceSupervision(SchoolDTO schoolDTO, String dateParam);
    MQResponseDto<List<StaffDailyAttendanceTaskSupervisionDTO>> cacheStaffDailyTimetableTaskSupervision(SchoolDTO schoolDTO , String dateParam);

    MQResponseDto<List<StaffDailyTimetableDTO>> cacheStaffDailyTimetables(SchoolDTO schoolDTO);
    MQResponseDto<List<DistrictDTO>> cacheDistricts();
    MQResponseDto<TimetableDTO> cacheSchoolTimetables(SchoolDTO schoolDTO);
}
