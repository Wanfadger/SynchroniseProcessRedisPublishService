package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.*;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.supervision.StaffDailyAttendanceTaskSupervisionDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.StaffDailyTimetableDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.TimetableDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.cache.CacheService;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.utils.TelaDatePattern;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.utils.publisher.QueueTopicPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        SchoolDTO schoolDTO = cacheService.cacheSchoolData(dto.telaSchoolNumber());
        try {
            MQResponseDto<SchoolDTO> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.SCHOOL);
            responseDto.setData(schoolDTO);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
            log.info("publishSchoolDatafor {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), responseDto);

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
            System.out.println(e);
        }





//
        return ResponseEntity.ok(new SystemAppFeedBack<>(true , "success" , true));
    }

    @Override
    @Async
    public void publishSchoolData(SynchronizeSchoolDataDTO dto) {
        SchoolDTO schoolDTO = cacheService.cacheSchoolData(dto.telaSchoolNumber());
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
        MQResponseDto<List<ClassDTO>> listMQResponseDto = cacheService.cacheSchoolClasses(schoolDTO);
//            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
        log.info("CLASES PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());

    }

    @Override
    @Async
    public void publishSchoolStaffs(SchoolDTO schoolDTO) throws JsonProcessingException {

        MQResponseDto<List<StaffDTO>> listMQResponseDto = cacheService.cacheSchoolStaffs(schoolDTO);
//            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
        log.info("STAFFS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());


    }

    @Override
    @Async
    public void publishSchoolClockIns(SchoolDTO schoolDTO, String dateParam) throws JsonProcessingException {
        MQResponseDto<List<ClockInDTO>> listMQResponseDto = cacheService.cacheSchoolTermClockIns(schoolDTO);


        if ("all".equalsIgnoreCase(dateParam)) {
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            System.out.println("school.getTelaSchoolUID() " +schoolDTO.getTelaSchoolNumber());
            log.info("CLOCKINS PUBLISHED for {} {} {} ", schoolDTO.getTelaSchoolNumber(), schoolDTO.getName(), listMQResponseDto.getData().size());
        } else {

            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<ClockInDTO> clockInDTOS = listMQResponseDto.getData().parallelStream().filter(clockInDTO -> {
                LocalDateTime dateTime = LocalDateTime.parse(clockInDTO.getClockInDateTime(), TelaDatePattern.dateTimePattern24);
                return dateTime.toLocalDate().equals(localDate);
            }).toList();

            listMQResponseDto.setData(clockInDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            System.out.println("school.getTelaSchoolUID() " +schoolDTO.getTelaSchoolNumber());
            log.info("CLOCKINS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        }


    }

    @Override
    @Async
    public void publishSubjects(SchoolDTO schoolDTO) throws JsonProcessingException {
        MQResponseDto<List<IdNameCodeDTO>> listMQResponseDto = cacheService.cacheSubjects(schoolDTO);
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
        log.info("Subjects PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());

    }

    @Override
    @Async
    public void publishLearnerEnrollments(SchoolDTO schoolDTO) throws JsonProcessingException {
        MQResponseDto<List<LearnerHeadCountDTO>> listMQResponseDto = cacheService.cacheLearnerEnrollments(schoolDTO);
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
        log.info("LEARNER_HEADCOUNTS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());

    }

    @Override
    @Async
    public void publishLearnerAttendance(SchoolDTO schoolDTO,  String dateParam) throws JsonProcessingException {

        MQResponseDto<List<LearnerAttendanceDTO>> listMQResponseDto = cacheService.cacheLearnerAttendance(schoolDTO);

        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("publishLearnerAttendance PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        } else {
            List<LearnerAttendanceDTO> dateLearnerAttendances = listMQResponseDto.getData();
            List<LearnerAttendanceDTO> learnerAttendanceDTOS = dateLearnerAttendances.parallelStream().filter(dto -> dto.getSubmissionDate().equals(dateParam)).toList();
            listMQResponseDto.setData(learnerAttendanceDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("publishLearnerAttendance PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        }
    }

    @Override
    @Async
    public void publishStaffDailyTimeAttendanceSupervision(SchoolDTO schoolDTO, String dateParam) throws JsonProcessingException {
        MQResponseDto<List<StaffDailyTimeAttendanceDTO>> listMQResponseDto = cacheService.cacheStaffDailyTimeAttendanceSupervision(schoolDTO, dateParam);

        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("STAFF_DAILY_TIME_ATTENDANCES published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<StaffDailyTimeAttendanceDTO> staffDailyTimeAttendanceDTOS = listMQResponseDto.getData().parallelStream().filter(dto -> {
                LocalDateTime dateTime = LocalDateTime.parse(dto.getSupervisionDateTime(), TelaDatePattern.dateTimePattern24);

                return dateTime.toLocalDate().equals(localDate);
            }).toList();
            listMQResponseDto.setData(staffDailyTimeAttendanceDTOS);

            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("STAFF_DAILY_TIME_ATTENDANCES published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        }


    }

    @Override
    @Async
    public void publishDistricts(SchoolDTO schoolDTO) throws JsonProcessingException {
        MQResponseDto<List<DistrictDTO>> listMQResponseDto = cacheService.cacheDistricts();
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
        log.info("publishDistricts PUBLISHED for {} ", listMQResponseDto.getData().size());
    }

    @Override
    @Async
    public void publishSchoolTimetables(SchoolDTO schoolDTO) throws JsonProcessingException {
        MQResponseDto<TimetableDTO> timetableDTOMQResponseDto = cacheService.cacheSchoolTimetables(schoolDTO);
        queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(timetableDTOMQResponseDto));
        log.info("SCHOOL_TIMETABLE published for {} \n {} \n {}  ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), new TimetableDTO());
    }

    @Override
    @Async
    public void publishStaffDailyTimetables(SchoolDTO schoolDTO , String dateParam) throws JsonProcessingException {
        MQResponseDto<List<StaffDailyTimetableDTO>> listMQResponseDto = cacheService.cacheStaffDailyTimetables(schoolDTO);

        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("STAFF_DAILY_TIMETABLES published for {} \n {} \n {}  ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        } else {
            List<StaffDailyTimetableDTO> staffDailyTimetableDTOS =
                    listMQResponseDto.getData().parallelStream().filter(staffDailyTimetableDTO -> staffDailyTimetableDTO.getSubmissionDate().equals(dateParam)).toList();
            listMQResponseDto.setData(staffDailyTimetableDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("STAFF_DAILY_TIMETABLES published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), staffDailyTimetableDTOS.size());
        }


    }

    @Override
    @Async
    public void publishStaffDailyTimetableTaskSupervision(SchoolDTO schoolDTO, String dateParam) throws JsonProcessingException {
        log.info("publishStaffDailyTimetableTaskSupervision");
        MQResponseDto<List<StaffDailyAttendanceTaskSupervisionDTO>> listMQResponseDto = cacheService.cacheStaffDailyTimetableTaskSupervision(schoolDTO, dateParam);

        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("STAFF_DAILY_TASK_SUPERVISION published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<StaffDailyAttendanceTaskSupervisionDTO> dailyAttendanceTaskSupervisionDTOS = listMQResponseDto.getData().parallelStream().filter(dto -> {
                LocalDate parse = LocalDate.parse(dto.getSupervisionDate(), TelaDatePattern.datePattern);
                return parse.equals(localDate);
            }).toList();
            listMQResponseDto.setData(dailyAttendanceTaskSupervisionDTOS);

            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("STAFF_DAILY_TASK_SUPERVISION published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        }

    }

    @Override
    @Async
    public void publishSchoolClockOuts(SchoolDTO schoolDTO , String dateParam) throws JsonProcessingException {
        MQResponseDto<List<ClockOutDTO>> listMQResponseDto = cacheService.cacheSchoolTermClockOuts(schoolDTO);

        if ("all".equalsIgnoreCase(dateParam)) {
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("CLOCKOUTS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<ClockOutDTO> dateClockOutDTOS = listMQResponseDto.getData().parallelStream().filter(clockOutDTO -> {
                LocalDateTime dateTime = LocalDateTime.parse(clockOutDTO.getClockOutDateTime(), TelaDatePattern.dateTimePattern24);
                return dateTime.toLocalDate().equals(localDate);
            }).toList();
            listMQResponseDto.setData(dateClockOutDTOS);
            queueTopicPublisher.publishTopicData(schoolDTO.getTelaSchoolNumber(), objectMapper.writeValueAsString(listMQResponseDto));
            log.info("CLOCKOUTS PUBLISHED for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), listMQResponseDto.getData().size());
        }
    }

}
