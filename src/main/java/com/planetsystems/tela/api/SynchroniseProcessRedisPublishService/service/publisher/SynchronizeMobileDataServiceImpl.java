package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.*;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.supervision.StaffDailyAttendanceTaskSupervisionDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.StaffDailyTimetableDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.TimetableDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.cache.CacheKeys;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.cache.CacheService;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.utils.TelaDatePattern;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.utils.publisher.QueueTopicPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SynchronizeMobileDataServiceImpl implements SynchronizeMobileDataService {
//    private final SchoolRepository schoolRepository;
//    private final AcademicTermRepository academicTermRepository;
//    private final SchoolClassRepository schoolClassRepository;
//    private final SchoolStaffRepository schoolStaffRepository;
//    final ClockInRepository clockInRepository;
//    final ClockOutRepository clockOutRepository;
//    final SubjectRepository subjectRepository;
//    final LearnerEnrollmentRepository learnerEnrollmentRepository;
//    final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
//    final LearnerAttendanceRepository learnerAttendanceRepository;
//    final SNLearnerAttendanceRepository snLearnerAttendanceRepository;
//    final DistrictRepository districtRepository;
//    final StaffDailyAttendanceSupervisionRepository staffDailyAttendanceSupervisionRepository;
//    final StaffDailyAttendanceTaskSupervisionRepository staffDailyAttendanceTaskSupervisionRepository;
//    final TimeTableRepository timeTableRepository;
//    final TimeTableLessonRepository timeTableLessonRepository;
//    final StaffDailyTimeTableRepository staffDailyTimeTableRepository;
//    final StaffDailyTimeTableLessonRepository staffDailyTimeTableLessonRepository;
//    final JmsTemplate jmsTemplate;
    final QueueTopicPublisher queueTopicPublisher;
    final ObjectMapper objectMapper;

    final CacheService cacheService;


    @Override
    public ResponseEntity<SystemAppFeedBack<Boolean>> synchronizeSchoolData(SynchronizeSchoolDataDTO dto) {
        log.info("synchronizeSchoolData {} " , dto);
//        String dateParam = dto.date();
//        IdProjection schoolIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(dto.telaNumber(), Status.DELETED).orElseThrow(() -> new TelaNotFoundException("School with " + dto.telaNumber() + " not found"));
//
//        School school = schoolRepository.findByStatusNotAndId(Status.DELETED, schoolIdProjection.getId()).orElseThrow(() -> new TelaNotFoundException("School not found"));
//        AcademicTerm academicTerm = academicTermRepository.activeAcademicTerm(Status.ACTIVE).orElseThrow(() -> new TelaNotFoundException("Active term not found"));
        // school information
        // school



        AcademicTermDTO academicTermDTO = cacheService.cacheActiveAcademicTerm();
        SchoolDTO schoolDTO = cacheService.cacheSchoolData(dto.telaSchoolNumber() ,academicTermDTO);

        log.info("subjects {} " , cacheService.cacheSubjects(schoolDTO));

        try {
//            MQResponseDto<SchoolDTO> responseDto = new MQResponseDto<>();
//            responseDto.setResponseType(ResponseType.SCHOOL);
//            responseDto.setData(schoolDTO);
//            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
//            log.info("publishSchoolDatafor {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), responseDto);
            publishSchoolData(schoolDTO);

//                 classes
            publishSchoolClasses(schoolDTO);
            //        // staff
        publishSchoolStaffs(schoolDTO);
            ////                subjects
        publishSubjects(schoolDTO);
            //        //publishLearnerEnrollments
        publishLearnerEnrollments(schoolDTO);

            //        // publishSchoolTimetables
//
        publishSchoolTimetables(schoolDTO);

            //        //publishStaffDailyTimetables
        publishStaffDailyTimetables(schoolDTO , dto.date());

            //        //publishLearnerAttendance
        publishLearnerAttendance(schoolDTO, dto.date());

            //        //publishDistricts
        publishDistricts(schoolDTO);

            //publishStaffDailyTimetableTaskSupervision
            publishStaffDailyTimetableTaskSupervision(schoolDTO, dto.date());

            //
//        //publishStaffDailyTimeAttendance
        publishStaffDailyTimeAttendanceSupervision(schoolDTO, dto.date());

            //        // clockins
        publishSchoolClockIns(schoolDTO, dto.date());

            //        //publishSchoolClockOuts
        publishSchoolClockOuts(schoolDTO, dto.date());


        } catch (Exception e) {
            e.printStackTrace();
           log.error(e.getMessage());
        }





//
        return ResponseEntity.ok(new SystemAppFeedBack<>(true , "success" , true));
    }

    @Override
    @Async
    public void publishSchoolData(SchoolDTO schoolDTO) {
        try {
            MQResponseDto<SchoolDTO> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.SCHOOL);
            responseDto.setData(schoolDTO);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("publishSchoolDatafor {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }



    @Override
    @Async
    public void publishSchoolClasses(SchoolDTO schoolDTO) throws JsonProcessingException {
        List<ClassDTO> classDTOS = cacheService.cacheSchoolClasses(schoolDTO);
        MQResponseDto<List<ClassDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.CLASSES);
        responseDto.setData(classDTOS);
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
        log.info("CLASES PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), classDTOS.size());

    }

    @Override
    @Async
    public void publishSchoolStaffs(SchoolDTO schoolDTO) throws JsonProcessingException {
        List<StaffDTO> staffDTOList = cacheService.cacheSchoolStaffs(schoolDTO);
        MQResponseDto<List<StaffDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.STAFFS);
        responseDto.setData(staffDTOList);
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(staffDTOList));
        log.info("STAFFS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), staffDTOList.size());
    }

    @Override
    @Async
    public void publishSchoolClockIns(SchoolDTO schoolDTO, String dateParam) throws JsonProcessingException {
        List<ClockInDTO> clockInDTOS = cacheService.cacheSchoolTermClockIns(schoolDTO);
        MQResponseDto<List<ClockInDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.CLOCKINS);


        if ("all".equalsIgnoreCase(dateParam)) {
            responseDto.setData(clockInDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            System.out.println("school.getTelaSchoolUID() " +schoolDTO.getTelaSchoolNumber());
            log.info("CLOCKINS PUBLISHED for {} {} {} ", schoolDTO.getTelaSchoolNumber(), schoolDTO.getName(), clockInDTOS.size());
        } else {

            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<ClockInDTO> localDateClockInDTOS = clockInDTOS.parallelStream().filter(clockInDTO -> {
                LocalDateTime dateTime = LocalDateTime.parse(clockInDTO.getClockInDateTime(), TelaDatePattern.dateTimePattern24);
                return dateTime.toLocalDate().equals(localDate);
            }).toList();

            responseDto.setData(localDateClockInDTOS);

            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            System.out.println("school.getTelaSchoolUID() " +schoolDTO.getTelaSchoolNumber());
            log.info("CLOCKINS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), localDateClockInDTOS.size());
        }


    }

    @Override
    @Async
    public void publishSubjects(SchoolDTO schoolDTO) throws JsonProcessingException {
        List<IdNameCodeDTO> subjectDTOS = cacheService.cacheSubjects(schoolDTO);

        MQResponseDto<List<IdNameCodeDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.SUBJECTS);
        responseDto.setData(subjectDTOS);
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
        log.info("Subjects PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), subjectDTOS.size());

    }

    @Override
    @Async
    public void publishLearnerEnrollments(SchoolDTO schoolDTO) throws JsonProcessingException {
        List<LearnerHeadCountDTO> generalLearnerHeadCountDTOS = cacheService.cacheLearnerEnrollments(schoolDTO);

        MQResponseDto<List<LearnerHeadCountDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.LEARNER_HEADCOUNTS);
        responseDto.setData(generalLearnerHeadCountDTOS);
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
        log.info("LEARNER_HEADCOUNTS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), generalLearnerHeadCountDTOS.size());

    }

    @Override
    @Async
    public void publishLearnerAttendance(SchoolDTO schoolDTO,  String dateParam) throws JsonProcessingException {

        List<LearnerAttendanceDTO> generalLearnerAttendanceDTOS = cacheService.cacheLearnerAttendance(schoolDTO);

        MQResponseDto<List<LearnerAttendanceDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.LEARNER_ATTENDANCES);



        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            responseDto.setData(generalLearnerAttendanceDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("publishLearnerAttendance PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(),generalLearnerAttendanceDTOS.size());
        } else {
            List<LearnerAttendanceDTO> dateLearnerAttendanceDTOS = generalLearnerAttendanceDTOS.parallelStream().filter(dto -> dto.getSubmissionDate().equals(dateParam)).toList();
            responseDto.setData(dateLearnerAttendanceDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("publishLearnerAttendance PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(),dateLearnerAttendanceDTOS.size());
        }
    }

    @Override
    @Async
    public void publishStaffDailyTimeAttendanceSupervision(SchoolDTO schoolDTO, String dateParam) throws JsonProcessingException {
        List<StaffDailyTimeAttendanceDTO> staffDailyTimeAttendanceDTOS = cacheService.cacheStaffDailyTimeAttendanceSupervision(schoolDTO, dateParam);

        MQResponseDto<List<StaffDailyTimeAttendanceDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.STAFF_DAILY_TIME_ATTENDANCES);


        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            responseDto.setData(staffDailyTimeAttendanceDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFF_DAILY_TIME_ATTENDANCES published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), staffDailyTimeAttendanceDTOS.size());
        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<StaffDailyTimeAttendanceDTO> dateStaffDailyTimeAttendanceDTOS = staffDailyTimeAttendanceDTOS.parallelStream().filter(dto -> {
                LocalDateTime dateTime = LocalDateTime.parse(dto.getSupervisionDateTime(), TelaDatePattern.dateTimePattern24);

                return dateTime.toLocalDate().equals(localDate);
            }).toList();
            responseDto.setData(dateStaffDailyTimeAttendanceDTOS);

            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFF_DAILY_TIME_ATTENDANCES published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), dateStaffDailyTimeAttendanceDTOS.size());
        }


    }

    @Override
    @Async
    public void publishDistricts(SchoolDTO schoolDTO) throws JsonProcessingException {
        List<DistrictDTO> districtDTOS = cacheService.cacheDistricts();

        MQResponseDto<List<DistrictDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.DISTRICTS);
        responseDto.setData(districtDTOS);

        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
        log.info("publishDistricts PUBLISHED for {} ", districtDTOS.size());
    }

    @CacheEvict(value = CacheKeys.DISTRICTS)
    @Override
    public void evictDistricts() {

    }

    @Override
    @Async
    public void publishSchoolTimetables(SchoolDTO schoolDTO) throws JsonProcessingException {
        TimetableDTO timetableDTO = cacheService.cacheSchoolTimetables(schoolDTO);

        MQResponseDto<TimetableDTO> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.SCHOOL_TIMETABLE);
        responseDto.setData(timetableDTO);

        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
        log.info("SCHOOL_TIMETABLE published for {} \n {} \n {}  ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), timetableDTO);
    }

    @Override
    @Async
    public void publishStaffDailyTimetables(SchoolDTO schoolDTO , String dateParam) throws JsonProcessingException {
        List<StaffDailyTimetableDTO> staffDailyTimetableDTOS = cacheService.cacheStaffDailyTimetables(schoolDTO);

        MQResponseDto<List<StaffDailyTimetableDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.STAFF_DAILY_TIMETABLES);


        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            responseDto.setData(staffDailyTimetableDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFF_DAILY_TIMETABLES published for {} \n {} \n {}  ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), staffDailyTimetableDTOS.size());
        } else {
            List<StaffDailyTimetableDTO> dateStaffDailyTimetableDTOS = staffDailyTimetableDTOS.parallelStream().filter(staffDailyTimetableDTO -> staffDailyTimetableDTO.getSubmissionDate().equals(dateParam)).toList();
            responseDto.setData(dateStaffDailyTimetableDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFF_DAILY_TIMETABLES published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), dateStaffDailyTimetableDTOS.size());
        }


    }

    @Override
    @Async
    public void publishStaffDailyTimetableTaskSupervision(SchoolDTO schoolDTO, String dateParam) throws JsonProcessingException {
        log.info("publishStaffDailyTimetableTaskSupervision");
        List<StaffDailyAttendanceTaskSupervisionDTO> staffDailyAttendanceTaskSupervisionDTOS = cacheService.cacheStaffDailyTimetableTaskSupervision(schoolDTO, dateParam);

        MQResponseDto<List<StaffDailyAttendanceTaskSupervisionDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.STAFF_DAILY_TASK_SUPERVISIONS);
        log.info("STAFF_DAILY_TASK_SUPERVISION published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), staffDailyAttendanceTaskSupervisionDTOS.size());


        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            responseDto.setData(staffDailyAttendanceTaskSupervisionDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFF_DAILY_TASK_SUPERVISION published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), staffDailyAttendanceTaskSupervisionDTOS.size());
        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<StaffDailyAttendanceTaskSupervisionDTO> dailyAttendanceTaskSupervisionDTOS = staffDailyAttendanceTaskSupervisionDTOS.parallelStream().filter(dto -> {
                LocalDate parse = LocalDate.parse(dto.getSupervisionDate(), TelaDatePattern.datePattern);
                return parse.equals(localDate);
            }).toList();
            responseDto.setData(dailyAttendanceTaskSupervisionDTOS);

            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFF_DAILY_TASK_SUPERVISION published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), dailyAttendanceTaskSupervisionDTOS.size());
        }

    }

    @Override
    @Async
    public void publishSchoolClockOuts(SchoolDTO schoolDTO , String dateParam) throws JsonProcessingException {
        List<ClockOutDTO> clockOutDTOS = cacheService.cacheSchoolTermClockOuts(schoolDTO);

        MQResponseDto<List<ClockOutDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.CLOCKOUTS);



        if ("all".equalsIgnoreCase(dateParam)) {
            responseDto.setData(clockOutDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("CLOCKOUTS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), clockOutDTOS.size());
        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<ClockOutDTO> dateClockOutDTOS = clockOutDTOS.parallelStream().filter(clockOutDTO -> {
                LocalDateTime dateTime = LocalDateTime.parse(clockOutDTO.getClockOutDateTime(), TelaDatePattern.dateTimePattern24);
                return dateTime.toLocalDate().equals(localDate);
            }).toList();
            responseDto.setData(dateClockOutDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("CLOCKOUTS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), clockOutDTOS.size());
        }
    }

}
