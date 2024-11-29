package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.*;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.supervision.StaffDailyAttendanceTaskSupervisionDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.ClassTimetableDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.StaffDailyTimetableDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.TimeTableLessonDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable.TimetableDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.exception.TelaNotFoundException;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.*;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.LearnerType;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.SchoolLevel;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.SubjectClassification;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository.*;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository.projections.ClockInProjection;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository.projections.IdProjection;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.utils.Convertor;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.utils.TelaDatePattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService{

    final AcademicTermRepository academicTermRepository;
    final SchoolRepository schoolRepository;
    final SchoolClassRepository schoolClassRepository;
    final SchoolStaffRepository schoolStaffRepository;
    final ClockInRepository clockInRepository;
    final SubjectRepository subjectRepository;
    final LearnerEnrollmentRepository learnerEnrollmentRepository;
    final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
    final TimeTableRepository timeTableRepository;
    final TimeTableLessonRepository timeTableLessonRepository;
    final StaffDailyTimeTableRepository staffDailyTimeTableRepository;
    final StaffDailyTimeTableLessonRepository staffDailyTimeTableLessonRepository;
    final LearnerAttendanceRepository learnerAttendanceRepository;
    final SNLearnerAttendanceRepository snLearnerAttendanceRepository;
    final DistrictRepository districtRepository;
    final ClockOutRepository clockOutRepository;
    final StaffDailyAttendanceSupervisionRepository staffDailyAttendanceSupervisionRepository;
    final StaffDailyAttendanceTaskSupervisionRepository staffDailyAttendanceTaskSupervisionRepository;

    final RedisTemplate<String , Object> redisTemplate;
    final ObjectMapper objectMapper;


    @Override
//    @Cacheable(value = CacheKeys.ACTIVE_ACADEMIC_TERM , cacheManager = "monthCacheManager")
    @Transactional(readOnly = true)
    public AcademicTermDTO cacheActiveAcademicTerm() {

        final String cacheKey = CacheKeys.ACTIVE_ACADEMIC_TERM;

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));
//        log.info("terms {} " , optionalCache.get());
        if (optionalCache.isPresent()) {
            AcademicTermDTO academicTermDTO = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return academicTermDTO;
        }


        AcademicTerm academicTerm = academicTermRepository.activeAcademicTerm(Status.ACTIVE).orElseThrow(() -> new TelaNotFoundException("Active term not found"));
        AcademicTermDTO academicTermDTO = AcademicTermDTO.builder()
                .id(academicTerm.getId())
                .name(academicTerm.getTerm())
                .year(academicTerm.getAcademicYear().getName())
                .startDate(academicTerm.getStartDate().format(TelaDatePattern.datePattern))
                .endDate(academicTerm.getEndDate().format(TelaDatePattern.datePattern))
                .build();

        redisTemplate.opsForValue().set(cacheKey , academicTermDTO , Duration.ofDays(30));
        return academicTermDTO;
    }


    @Override
//    @Cacheable(value = CacheKeys.SCHOOL , key = "#telaSchoolNumber" , cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public SchoolDTO cacheSchoolData(String telaSchoolNumber , AcademicTermDTO academicTermDTO) {
       try {
          final String cacheKey = CacheKeys.SCHOOL+"::"+telaSchoolNumber;

           Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

           if (optionalCache.isPresent()) {
               SchoolDTO schoolDTO = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
               return schoolDTO;
           }


           School school = schoolRepository.byTelaNumberOrDeviceNumber(Status.DELETED , telaSchoolNumber , telaSchoolNumber).orElseThrow(() -> new TelaNotFoundException("School not found"));

           SchoolDTO schoolDTO = SchoolDTO.builder()
                   .id(school.getId())
                   .academicTerm(academicTermDTO)
                   .district(new IdNameDTO(school.getDistrict().getId(), school.getDistrict().getName()))
                   .name(school.getName())
                   .telaSchoolNumber(school.getTelaSchoolUID())
                   .phoneNumber(school.getDeviceNumber())
                   .schoolLevel(school.getSchoolLevel().getLevel())
                   .schoolOwnership(school.getSchoolOwnership().getSchoolOwnership())
                   .licensed(school.getLicensed() != null ? school.getLicensed() : false)
                   .build();


           Optional<SchoolGeoCoordinate> optionalSchoolGeoCoordinate = school.getSchoolGeoCoordinateList().parallelStream().findFirst();

           if (optionalSchoolGeoCoordinate.isPresent()) {
               SchoolGeoCoordinate schoolGeoCoordinate = optionalSchoolGeoCoordinate.get();
               schoolDTO.setGeoCoordinate(GeoCoordinateDTO.builder()
                       .id(schoolGeoCoordinate.getId())
                       .pinClockActivated(schoolGeoCoordinate.isPinClockActivated())
                       .maxDisplacement(schoolGeoCoordinate.getDisplacement())
                       .geoFenceActivated(schoolGeoCoordinate.isGeoFenceActivated())
                       .longitude(schoolGeoCoordinate.getLongtitude())
                       .latitude(schoolGeoCoordinate.getLatitude())
                       .build());
           }


           redisTemplate.opsForValue().set(cacheKey , schoolDTO , Duration.ofDays(30));
           return schoolDTO;
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }

    @Override
//    @Cacheable(value = CacheKeys.CLASSES , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}", cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<ClassDTO> cacheSchoolClasses(SchoolDTO schoolDTO) {

       final String cacheKey = CacheKeys.CLASSES+"::"+"school="+schoolDTO.getTelaSchoolNumber()+",term="+schoolDTO.getAcademicTerm().getId();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            List<ClassDTO> cacheClassDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return cacheClassDTOS;
        }


            List<ClassDTO> classDTOS = schoolClassRepository
                    .findAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status.DELETED, schoolDTO.getAcademicTerm().getId(), schoolDTO.getId())
                    .parallelStream().map(schoolClass -> {
                        ClassDTO dto = new ClassDTO();
                        dto.setName(schoolClass.getName());
                        dto.setId(schoolClass.getId());
                        if (schoolClass.getParentSchoolClass() != null) {
                            dto.setParentSchoolClassId(schoolClass.getParentSchoolClass().getId());
                        } else {
                            dto.setParentSchoolClassId(null);
                        }

                        return dto;
                    }).sorted(Comparator.comparing(ClassDTO::getName))
                    .collect(Collectors.toList());

            if (classDTOS.isEmpty()) {
                List<SchoolClass> defaultClasses = this.generateDefaultClasses(SchoolLevel.getSchoolLevel(schoolDTO.getSchoolLevel()))
                        .stream().map(schoolClass -> {
                            schoolClass.setCreatedDateTime(LocalDateTime.now());
                            schoolClass.setUpdatedDateTime(LocalDateTime.now());
                            schoolClass.setAcademicTerm(new AcademicTerm(schoolDTO.getAcademicTerm().getId()));
                            schoolClass.setSchool(new School(schoolDTO.getId()));
                            schoolClass.setStatus(Status.ACTIVE);
                            return schoolClass;
                        }).collect(Collectors.toList());

                // create new
                schoolClassRepository.saveAll(defaultClasses);

                classDTOS = schoolClassRepository
                        .findAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status.DELETED, schoolDTO.getAcademicTerm().getId(), schoolDTO.getId())
                        .parallelStream().map(schoolClass -> {
                            ClassDTO dto = new ClassDTO();
                            dto.setName(schoolClass.getName());
                            dto.setId(schoolClass.getId());
                            if (schoolClass.getParentSchoolClass() != null) {
                                dto.setParentSchoolClassId(schoolClass.getParentSchoolClass().getId());
                            } else {
                                dto.setParentSchoolClassId(null);
                            }

                            return dto;
                        }).sorted(Comparator.comparing(ClassDTO::getName))
                        .collect(Collectors.toList());

            }

        redisTemplate.opsForValue().set(cacheKey , classDTOS , Duration.ofDays(30));
            return classDTOS;
    }

    private List<SchoolClass> generateDefaultClasses(SchoolLevel level) {
        IntStream priStream = IntStream.rangeClosed(1, 7);
        IntStream secStream = IntStream.rangeClosed(1, 6);
        IntStream caStream = IntStream.rangeClosed(1, 2);

        switch (level) {
            case PRIMARY:
                List<SchoolClass> pri = priStream.mapToObj(i -> {
                    SchoolClass dto = new SchoolClass();
                    String name = "P." + i;
                    dto.setName(name);
                    dto.setCode(name);
                    dto.setClassLevel(true);
                    dto.setHasStreams(false);

                    return dto;
                }).collect(Collectors.toList());

                return pri;

            case SECONDARY:
                List<SchoolClass> sec = secStream.mapToObj(i -> {
                    SchoolClass dto = new SchoolClass();
                    String name = "S." + i;
                    dto.setCode(name);
                    dto.setName(name);
                    dto.setClassLevel(true);
                    dto.setHasStreams(false);
                    return dto;
                }).collect(Collectors.toList());

                return sec;


            case CAI:
                List<SchoolClass> collect = caStream.mapToObj(i -> {
                    SchoolClass dto = new SchoolClass();
                    String name = "Year " + i;
                    dto.setCode(name);
                    dto.setName(name);
                    dto.setClassLevel(true);
                    dto.setHasStreams(false);
                    return dto;
                }).collect(Collectors.toList());

                return collect;


            default:
                System.out.println("Unknown School Level");
                return Collections.emptyList();
        }

    }

    @Override
//    @Cacheable(value = CacheKeys.STAFFS , key = "#schoolDTO.telaSchoolNumber", cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<StaffDTO> cacheSchoolStaffs(SchoolDTO schoolDTO) {
        final String cacheKey = CacheKeys.STAFFS+"::"+"school="+schoolDTO.getTelaSchoolNumber();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            List<StaffDTO> staffDTOList = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return staffDTOList;
        }


        List<StaffDTO> staffDTOList = schoolStaffRepository.findAllBySchoolWithSchool_StaffDetail(Status.DELETED, schoolDTO.getId())
                .parallelStream()
                .map(schoolStaff -> {
                    StaffDTO staffDTO = new StaffDTO();
                    staffDTO.setId(schoolStaff.getId());
                    staffDTO.setOnPayRoll(schoolStaff.isRegistered() ? "Yes" : "No");


                    GeneralUserDetail generalUserDetail = schoolStaff.getGeneralUserDetail();
                    if (generalUserDetail != null) {


                        if (generalUserDetail.getFirstName() != null) {
                            staffDTO.setFirstName(generalUserDetail.getFirstName());
                        }

                        if (generalUserDetail.getLastName() != null) {
                            staffDTO.setLastName(generalUserDetail.getLastName());
                        }


                        if (generalUserDetail.getDob() != null) {
                            staffDTO.setDob(generalUserDetail.getDob().format(TelaDatePattern.datePattern));
                        } else {
                            staffDTO.setDob("");
                        }

                        if (generalUserDetail.getEmail() != null) {
                            staffDTO.setEmailAddress(generalUserDetail.getEmail());
                        } else {
                            staffDTO.setEmailAddress("");
                        }

                        if (schoolStaff.getStaffCode() != null) {
                            staffDTO.setEmployeeNumber(schoolStaff.getStaffCode());
                        } else {
                            staffDTO.setEmployeeNumber("");
                        }


                        if (generalUserDetail.getGender() != null) {
                            staffDTO.setGender(generalUserDetail.getGender().getGender());
                        } else {
                            staffDTO.setGender("");
                        }

                        if (generalUserDetail.getNameAbbrev() != null) {
                            staffDTO.setInitials(generalUserDetail.getNameAbbrev());
                        } else {
                            staffDTO.setInitials("");
                        }


                        staffDTO.setLicensed(schoolStaff.isRegistered());

                        if (generalUserDetail.getNationalId() != null) {
                            staffDTO.setNationalId(generalUserDetail.getNationalId());
                        } else {
                            staffDTO.setNationalId("");
                        }

                        if (generalUserDetail.getPhoneNumber() != null) {
                            staffDTO.setPhoneNumber(generalUserDetail.getPhoneNumber());
                        } else {
                            staffDTO.setPhoneNumber(schoolStaff.getStaffCode());
                        }

                        if (schoolStaff.getStaffType() != null) {
                            staffDTO.setRole(schoolStaff.getStaffType().getStaffType());
                        } else {
                            staffDTO.setRole("");
                        }

                        staffDTO.setHasSpecialNeeds((schoolStaff.getSpecialNeeds() == null || schoolStaff.getSpecialNeeds()) ? "true" : "false");
                        staffDTO.setStaffType(schoolStaff.isTeachingstaff() ? "Teaching" : "Non-Teaching");

                        staffDTO.setExpectedHours(schoolStaff.getExpectedHours() > 8 ? 8 : schoolStaff.getExpectedHours());
                    }

                    return staffDTO;
                })
                .sorted(Comparator.comparing(StaffDTO::getFirstName))
                .toList();

        redisTemplate.opsForValue().set(cacheKey , staffDTOList , Duration.ofDays(30));
        return staffDTOList;
    }

    @Override
//    @Cacheable(value = CacheKeys.CLOCKINS , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}", cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<ClockInDTO> cacheSchoolTermClockIns(SchoolDTO schoolDTO) {

        final String cacheKey = CacheKeys.CLOCKINS+"::"+"school="+schoolDTO.getTelaSchoolNumber()+",term="+schoolDTO.getAcademicTerm().getId();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            List<ClockInDTO> clockInDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return clockInDTOS;
        }


        List<ClockInProjection> schoolTermClockIns = clockInRepository.nativeAllByTerm_School(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());

        List<ClockInDTO> clockInDTOS = schoolTermClockIns.parallelStream().map(clockIn -> {

                    LocalDateTime clockInDateTime = LocalDateTime.of(clockIn.getClockInDate(), clockIn.getClockInTime());

                    ClockInDTO clockInDTO = ClockInDTO.builder()
                            .id(clockIn.getId())
                            .displacement(clockIn.getDisplacement())
                            .clockInDateTime(clockInDateTime.format(TelaDatePattern.dateTimePattern24))
                            .clockInType(clockIn.getClockinType())
                            .staffId(clockIn.getStaffId())
                            .academicTermId(schoolDTO.getAcademicTerm().getId())
                            .longitude(clockIn.getLongitude())
                            .latitude(clockIn.getLatitude())
                            .telaSchoolNumber(schoolDTO.getTelaSchoolNumber())
                            .build();

                    return clockInDTO;
                })
                .sorted(Comparator.comparing(ClockInDTO::getClockInDateTime))
                .toList();

        redisTemplate.opsForValue().set(cacheKey , clockInDTOS , Duration.ofDays(7));
        return clockInDTOS;
    }

    @Override
//    @Cacheable(value = CacheKeys.CLOCKOUTS , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}", cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<ClockOutDTO> cacheSchoolTermClockOuts(SchoolDTO schoolDTO) {

        final String cacheKey = CacheKeys.CLOCKOUTS+"::"+"school="+schoolDTO.getTelaSchoolNumber()+",term="+schoolDTO.getAcademicTerm().getId();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            log.info("cacheSchoolTermClockOuts {} " , optionalCache.get());
            List<ClockOutDTO> clockOutDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return clockOutDTOS;
        }


        List<ClockOut> schoolClockOuts  = clockOutRepository.allByTerm_SchoolWithStaff(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());
        List<ClockOutDTO> clockOutDTOS = schoolClockOuts.parallelStream().map(clockOut -> {

                    LocalDateTime clockOutDateTime = LocalDateTime.of(clockOut.getClockOutDate(), clockOut.getClockOutTime());

                    ClockIn clockIn = clockOut.getClockIn();
                    ClockOutDTO clockOutDTO = ClockOutDTO.builder()
                            .clockInId(clockIn.getId())
                            .telaSchoolNumber(schoolDTO.getTelaSchoolNumber())
                            .academicTermId(schoolDTO.getAcademicTerm().getId())
                            .staffId(clockIn.getSchoolStaff().getId())
                            .id(clockOut.getId())
                            .clockOutType(clockOut.getClockOutType())
                            .clockOutDateTime(clockOutDateTime.format(TelaDatePattern.dateTimePattern24))
                            .reason(clockOut.getComment())
                            .latitude(clockIn.getLatitude())
                            .longitude(clockIn.getLongitude())
                            .comment(clockOut.getComment())
                            .displacement(clockOut.getDisplacement())
                            .build();

                    return clockOutDTO;
                })
                .sorted(Comparator.comparing(ClockOutDTO::getClockOutDateTime))
                .toList();
        redisTemplate.opsForValue().set(cacheKey , clockOutDTOS , Duration.ofDays(7));
        return clockOutDTOS;
    }

    @Override
//    @Cacheable(value = CacheKeys.SUBJECTS , key = "{'term='+#schoolDTO.academicTerm.id}",cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<IdNameCodeDTO> cacheSubjects(SchoolDTO schoolDTO) {
        final String cacheKey = CacheKeys.SUBJECTS;

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            log.info("cacheSubjects {} " , optionalCache.get());
            List<IdNameCodeDTO> subjectDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return subjectDTOS;
        }

        SchoolLevel schoolLevel = SchoolLevel.getSchoolLevel(schoolDTO.getSchoolLevel());
        SubjectClassification subjectClassification = SubjectClassification.getSubjectClassification(schoolLevel.getLevel());
        List<IdNameCodeDTO> subjectDTOS = subjectRepository.findAllBySubjectClassificationNotNullAndStatusNotAndSubjectClassification(Status.DELETED, subjectClassification)
                .parallelStream().map(subject -> new IdNameCodeDTO(subject.getId(), subject.getName(), subject.getCode()))
                .sorted(Comparator.comparing(IdNameCodeDTO::code))
                .toList();

        redisTemplate.opsForValue().set(cacheKey , subjectDTOS , Duration.ofDays(30));
        return subjectDTOS;
    }



    @Override
//    @Cacheable(value = CacheKeys.LEARNER_HEADCOUNTS , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}", cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<LearnerHeadCountDTO> cacheLearnerEnrollments(SchoolDTO schoolDTO) {
        final String cacheKey = CacheKeys.LEARNER_HEADCOUNTS+"::"+"school="+schoolDTO.getTelaSchoolNumber()+",term="+schoolDTO.getAcademicTerm().getId();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            log.info("cacheLearnerEnrollments {} " , optionalCache.get());
            List<LearnerHeadCountDTO> generalLearnerHeadCountDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return generalLearnerHeadCountDTOS;
        }


        List<LearnerHeadCountDTO> generalLearnerHeadCountDTOS = learnerEnrollmentRepository.allBySchool_term(schoolDTO.getId(), schoolDTO.getAcademicTerm().getId()).parallelStream()
                .map(enrollment -> {
                    LearnerHeadCountDTO learnerHeadCountDTO = LearnerHeadCountDTO.builder()
                            .staffId(enrollment.getSchoolStaff() != null ? enrollment.getSchoolStaff().getId() : "")
                            .classId(enrollment.getSchoolClass().getId())
                            .learnerType(LearnerType.GENERAL.getType())
                            .totalFemale(enrollment.getTotalGirls())
                            .totalMale(enrollment.getTotalBoys())
                            .submissionDate(enrollment.getSubmissionDate().format(TelaDatePattern.datePattern))
                            .build();

                    return learnerHeadCountDTO;
                }).collect(Collectors.toList());

        List<LearnerHeadCountDTO> snLearnerHeadCountDTOS = snLearnerEnrollmentRepository.allBySchool_term(schoolDTO.getId(), schoolDTO.getAcademicTerm().getId()).parallelStream()
                .map(enrollment -> {
                    LearnerHeadCountDTO learnerHeadCountDTO = LearnerHeadCountDTO.builder()
                            .staffId(enrollment.getSchoolStaff() != null ? enrollment.getSchoolStaff().getId() : "")
                            .classId(enrollment.getSchoolClass().getId())
                            .learnerType(LearnerType.SPECIAL_NEEDS.getType())
                            .totalFemale(enrollment.getTotalGirls())
                            .totalMale(enrollment.getTotalBoys())
                            .submissionDate(enrollment.getSubmissionDate().format(TelaDatePattern.datePattern))
                            .build();

                    return learnerHeadCountDTO;
                }).toList();
        generalLearnerHeadCountDTOS.addAll(snLearnerHeadCountDTOS);
        redisTemplate.opsForValue().set(cacheKey , generalLearnerHeadCountDTOS , Duration.ofDays(30));
        return generalLearnerHeadCountDTOS;
    }

    @Override
//    @Cacheable(value = CacheKeys.LEARNER_ATTENDANCES , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}" , cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public  List<LearnerAttendanceDTO> cacheLearnerAttendance(SchoolDTO schoolDTO) {
        final String cacheKey = CacheKeys.LEARNER_ATTENDANCES+"::"+"school="+schoolDTO.getTelaSchoolNumber()+",term="+schoolDTO.getAcademicTerm().getId();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            log.info("cacheLearnerAttendance {} " , optionalCache.get());
            List<LearnerAttendanceDTO> generalLearnerAttendanceDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return generalLearnerAttendanceDTOS;
        }

        List<LearnerAttendance> learnerAttendanceList = learnerAttendanceRepository.allByTerm_School(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());
        List<SNLearnerAttendance> snLearnerAttendanceList = snLearnerAttendanceRepository.allByTerm_School(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());

        List<LearnerAttendanceDTO> generalLearnerAttendanceDTOS = learnerAttendanceList.parallelStream().map(learnerAttendance -> {
                    LearnerAttendanceDTO learnerAttendanceDTO = LearnerAttendanceDTO.builder()
                            .staffId(learnerAttendance.getSchoolStaff().getId())
                            .classId(learnerAttendance.getSchoolClass().getId())
                            .comment(learnerAttendance.getComment())
                            .maleAbsent(learnerAttendance.getBoysAbsent())
                            .femaleAbsent(learnerAttendance.getGirlsAbsent())
                            .malePresent(learnerAttendance.getBoysPresent())
                            .femalePresent(learnerAttendance.getGirlsPresent())
                            .learnerType(LearnerType.GENERAL.getType())
                            .submissionDate(learnerAttendance.getAttendanceDate().format(TelaDatePattern.datePattern))
                            .build();
                    return learnerAttendanceDTO;
                }).sorted(Comparator.comparing(LearnerAttendanceDTO::getSubmissionDate))
                .collect(Collectors.toList());

        List<LearnerAttendanceDTO> snLearnerAttendanceDTOS = snLearnerAttendanceList.parallelStream().map(learnerAttendance -> {
                    LearnerAttendanceDTO snLearnerAttendanceDTO = LearnerAttendanceDTO.builder()
                            .staffId(learnerAttendance.getSchoolStaff().getId())
                            .classId(learnerAttendance.getSchoolClass().getId())
                            .comment(learnerAttendance.getComment())
                            .maleAbsent(learnerAttendance.getBoysAbsent())
                            .femaleAbsent(learnerAttendance.getGirlsAbsent())
                            .malePresent(learnerAttendance.getBoysPresent())
                            .femalePresent(learnerAttendance.getGirlsPresent())
                            .learnerType(LearnerType.GENERAL.getType())
                            .submissionDate(learnerAttendance.getAttendanceDate().format(TelaDatePattern.datePattern))
                            .build();
                    return snLearnerAttendanceDTO;
                }).sorted(Comparator.comparing(LearnerAttendanceDTO::getSubmissionDate))
                .toList();

        generalLearnerAttendanceDTOS.addAll(snLearnerAttendanceDTOS);
        redisTemplate.opsForValue().set(cacheKey , generalLearnerAttendanceDTOS , Duration.ofDays(30));
        return generalLearnerAttendanceDTOS;

    }

    @Override
//    @Cacheable(value = CacheKeys.STAFF_DAILY_TIME_ATTENDANCES , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}", cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<StaffDailyTimeAttendanceDTO> cacheStaffDailyTimeAttendanceSupervision(SchoolDTO schoolDTO, String dateParam) {
        final String cacheKey = CacheKeys.STAFF_DAILY_TIME_ATTENDANCES+"::"+"school="+schoolDTO.getTelaSchoolNumber()+",term="+schoolDTO.getAcademicTerm().getId();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            log.info("cacheStaffDailyTimeAttendanceSupervision {} " , optionalCache.get());
            List<StaffDailyTimeAttendanceDTO> staffDailyTimeAttendanceDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return staffDailyTimeAttendanceDTOS;
        }


        List<StaffDailyAttendanceSupervision> staffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository
                .allByTermDates_School(LocalDate.parse(schoolDTO.getAcademicTerm().getStartDate() , TelaDatePattern.datePattern), LocalDate.parse(schoolDTO.getAcademicTerm().getEndDate() , TelaDatePattern.datePattern)
                        , schoolDTO.getId());


        List<StaffDailyTimeAttendanceDTO> staffDailyTimeAttendanceDTOS = staffDailyAttendanceSupervisions.parallelStream().map(attendanceSupervision -> {

            LocalDateTime supervisionDateTime = LocalDateTime.of(attendanceSupervision.getSupervisionDate(), attendanceSupervision.getSupervisionTime());

            StaffDailyTimeAttendanceDTO staffDailyTimeAttendanceDTO = StaffDailyTimeAttendanceDTO.builder()
                    .id(attendanceSupervision.getId())
                    .staffId(attendanceSupervision.getSchoolStaff().getId())
                    .supervisorId(attendanceSupervision.getSupervisor().getId())
                    .supervisorComment(attendanceSupervision.getComment())
                    .supervisionStatus(attendanceSupervision.getAttendanceStatus().getAttendance())
                    .supervisionDateTime(supervisionDateTime.format(TelaDatePattern.dateTimePattern24))
                    .build();
            return staffDailyTimeAttendanceDTO;
        }).sorted(Comparator.comparing(StaffDailyTimeAttendanceDTO::getSupervisionDateTime)).toList();

        redisTemplate.opsForValue().set(cacheKey , staffDailyTimeAttendanceDTOS , Duration.ofDays(30));
        return  staffDailyTimeAttendanceDTOS;
    }

    @Override
//    @Cacheable(value = CacheKeys.STAFF_DAILY_TASK_SUPERVISIONS , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}", cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<StaffDailyAttendanceTaskSupervisionDTO> cacheStaffDailyTimetableTaskSupervision(SchoolDTO schoolDTO , String dateParam) {
        final String cacheKey = CacheKeys.STAFF_DAILY_TASK_SUPERVISIONS+"::"+"school="+schoolDTO.getTelaSchoolNumber()+",term="+schoolDTO.getAcademicTerm().getId();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            log.info("cacheStaffDailyTimetableTaskSupervision {} " , optionalCache.get());
            List<StaffDailyAttendanceTaskSupervisionDTO> staffDailyAttendanceTaskSupervisionDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return staffDailyAttendanceTaskSupervisionDTOS;
        }

        LocalDate startDate = LocalDate.parse(schoolDTO.getAcademicTerm().getStartDate(), TelaDatePattern.datePattern);
        LocalDate endDate = LocalDate.parse(schoolDTO.getAcademicTerm().getEndDate(), TelaDatePattern.datePattern);


        List<StaffDailyAttendanceSupervision> termStaffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository
                .allByTermDates_School(startDate, endDate, schoolDTO.getId());
        List<StaffDailyAttendanceTaskSupervision> termStaffDailyAttendanceTaskSupervisions = staffDailyAttendanceTaskSupervisionRepository.allIn(termStaffDailyAttendanceSupervisions);


        List<StaffDailyAttendanceTaskSupervisionDTO> staffDailyAttendanceTaskSupervisionDTOS = termStaffDailyAttendanceSupervisions.parallelStream().flatMap(attendanceSupervision -> termStaffDailyAttendanceTaskSupervisions.parallelStream()
                        .filter(taskSupervision -> attendanceSupervision.getId().equals(taskSupervision.getStaffDailyAttendanceSupervision().getId()))
                        .map(taskSupervision -> {

                            StaffDailyAttendanceTaskSupervisionDTO dto = StaffDailyAttendanceTaskSupervisionDTO.builder()
                                    .id(taskSupervision.getId())
                                    .timeAttendanceId(attendanceSupervision.getId())
                                    .lessonTaskId(taskSupervision.getStaffDailyTimeTableLesson().getId())
                                    .supervisorId(attendanceSupervision.getSupervisor().getId())
                                    .supervisionDate(attendanceSupervision.getSupervisionDate().format(TelaDatePattern.datePattern))
                                    .supervisionTime(attendanceSupervision.getSupervisionTime().format(TelaDatePattern.timePattern24))
                                    .timeStatus(taskSupervision.getTeachingTimeStatus().getSupervision())
                                    .supervisionComment(taskSupervision.getComment())
                                    .build();
                            return dto;
                        })
                ).sorted(Comparator.comparing(StaffDailyAttendanceTaskSupervisionDTO::getSupervisionDate))
                .toList();
        redisTemplate.opsForValue().set(cacheKey , staffDailyAttendanceTaskSupervisionDTOS , Duration.ofDays(30));
        return staffDailyAttendanceTaskSupervisionDTOS;
    }

    @Override
//    @Cacheable(value = CacheKeys.STAFF_DAILY_TIMETABLES , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}", cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<StaffDailyTimetableDTO> cacheStaffDailyTimetables(SchoolDTO schoolDTO) {
        final String cacheKey = CacheKeys.STAFF_DAILY_TIMETABLES+"::"+"school="+schoolDTO.getTelaSchoolNumber()+",term="+schoolDTO.getAcademicTerm().getId();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            log.info("cacheStaffDailyTimetables {} " , optionalCache.get());
            List<StaffDailyTimetableDTO> staffDailyTimetableDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return staffDailyTimetableDTOS;
        }

        List<StaffDailyTimeTable> termStaffDailyTimeTables = staffDailyTimeTableRepository.allByTerm_School(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());
        List<StaffDailyTimeTableLesson> termStaffDailyTimeTableLessons = staffDailyTimeTableLessonRepository.allIn(termStaffDailyTimeTables);

        List<StaffDailyTimetableDTO> staffDailyTimetableDTOS = termStaffDailyTimeTables.parallelStream().flatMap(dailyTimeTable -> termStaffDailyTimeTableLessons.parallelStream()
                .filter(dailyLesson -> dailyTimeTable.getId().equals(dailyLesson.getStaffDailyTimeTable().getId()))
                .map(dailyLesson -> {
                    StaffDailyTimetableDTO dto = StaffDailyTimetableDTO.builder()
                            .id(dailyLesson.getId())
                            .timeAttendanceId(dailyTimeTable.getId())
                            .subjectId(dailyLesson.getSubject().getId())
                            .classId(dailyLesson.getSchoolClass().getId())
                            .startTime(dailyLesson.getStartTime().format(TelaDatePattern.timePattern24))
                            .endTime(dailyLesson.getEndTime().format(TelaDatePattern.timePattern24))
                            .actionStatus(dailyLesson.getDailyTimeTableLessonStatus().getAttendance())
                            .submissionDate(dailyLesson.getLessonDate().format(TelaDatePattern.datePattern))
                            .staffId(dailyTimeTable.getSchoolStaff().getId())
                            .comment(dailyTimeTable.getComment())
                            .build();
                    return dto;
                })
        ).sorted(Comparator.comparing(StaffDailyTimetableDTO::getStaffId)).toList();
        redisTemplate.opsForValue().set(cacheKey , staffDailyTimetableDTOS , Duration.ofDays(30));
        return staffDailyTimetableDTOS;

    }

    @Override
//    @Cacheable(value = CacheKeys.DISTRICTS, cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public List<DistrictDTO> cacheDistricts() {
        final String cacheKey = CacheKeys.DISTRICTS;

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            log.info("cacheDistricts {} " , optionalCache.get());
            List<DistrictDTO> districtDTOS = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return districtDTOS;
        }

        List<DistrictDTO> districtDTOS = districtRepository.findAllByStatusNot(Status.DELETED)
                .parallelStream()
                .map(district -> new DistrictDTO(district.getId(), district.getName(), district.getRegion().getName()))
                .sorted(Comparator.comparing(DistrictDTO::name))
                .toList();

        redisTemplate.opsForValue().set(cacheKey , districtDTOS , Duration.ofDays(30));
        return districtDTOS;
    }



    @Override
//    @Cacheable(value = CacheKeys.SCHOOL_TIMETABLE , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}" , cacheManager = "halfHourCacheManager")
    @Transactional(readOnly = true)
    public TimetableDTO cacheSchoolTimetables(SchoolDTO schoolDTO) {
        final String cacheKey = CacheKeys.SCHOOL_TIMETABLE+"::"+"school="+schoolDTO.getTelaSchoolNumber()+",term="+schoolDTO.getAcademicTerm().getId();

        Optional<Object> optionalCache = Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey));

        if (optionalCache.isPresent()) {
            log.info("cacheStaffDailyTimetableTaskSupervision {} " , optionalCache.get());
            TimetableDTO timetableDTO = objectMapper.convertValue(optionalCache.get(), new TypeReference<>() {});
            return timetableDTO;
        }


        Optional<IdProjection> idProjectionOptional = timeTableRepository.findBySchool_IdAndAcademicTerm_Id(schoolDTO.getId(), schoolDTO.getAcademicTerm().getId());

//                .orElseThrow(() -> new TelaNotFoundException(school.getName() + "Timetable not found from " + academicTerm.getTerm()));

        if (idProjectionOptional.isPresent()) {
            IdProjection idProjection = idProjectionOptional.get();

            TimetableDTO timetableDTO = TimetableDTO.builder()
                    .schoolId(schoolDTO.getId())
                    .academicTermId(schoolDTO.getAcademicTerm().getId())
                    .id(idProjection.getId())
                    .build();

            Map<String, List<TimeTableLesson>> classLessonMap = timeTableLessonRepository.allByTimetable(idProjection.getId()).parallelStream()
                    .filter(Convertor.distinctByKey(timeTableLesson -> timeTableLesson.getStartTime() + "" + timeTableLesson.getEndTime() + "" + timeTableLesson.getLessonDay() + "" + timeTableLesson.getSubject().getId()))
                    .collect(Collectors.groupingBy(timeTableLesson -> timeTableLesson.getSchoolClass().getId()));

            List<ClassTimetableDTO> classTimetableDTOS = classLessonMap.keySet().parallelStream().map(classId -> {
                List<TimeTableLesson> classLessons = classLessonMap.get(classId);
                Optional<TimeTableLesson> firstOptional = classLessons.parallelStream().findFirst();

                ClassTimetableDTO classDTO = ClassTimetableDTO.builder()
                        .classId(classId)
                        .build();

                if (firstOptional.isPresent()) {
                    TimeTableLesson lesson = firstOptional.get();
                    classDTO.setClassEndTime(lesson.getClassEndTime() != null ? lesson.getClassEndTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setClassStartTime(lesson.getClassStartTime() != null ? lesson.getClassStartTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setDuration(lesson.getDuration() != null ? lesson.getDuration() : 0);
                    classDTO.setBreakStartTime(lesson.getBreakStartTime() != null ? lesson.getBreakStartTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setBreakEndTime(lesson.getBreakStartTime() != null ? lesson.getBreakStartTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setLunchEndTime(lesson.getBreakStartTime() != null ? lesson.getBreakStartTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setLunchStartTime(lesson.getLunchStartTime() != null ? lesson.getLunchStartTime().format(TelaDatePattern.timePattern24) : "");
                }

                List<TimeTableLessonDTO> lessonDTOS = classLessons.parallelStream().map(tLesson -> {
                    TimeTableLessonDTO lessonDTO = TimeTableLessonDTO.builder()
                            .endTime(tLesson.getEndTime() != null ? tLesson.getEndTime().format(TelaDatePattern.timePattern24) : "")
                            .startTime(tLesson.getStartTime() != null ? tLesson.getStartTime().format(TelaDatePattern.timePattern24) : "")
                            .lessonDay(tLesson.getLessonDay().getDay())
                            .id(tLesson.getId())
                            .subjectId(tLesson.getSubject().getId())
                            .staffId(tLesson.getSchoolStaff().getId())
                            .build();
                    return lessonDTO;
                }).toList();

                classDTO.setLessons(lessonDTOS);

                return classDTO;
            }).toList();

            timetableDTO.setClassTimetables(classTimetableDTOS);
            redisTemplate.opsForValue().set(cacheKey , timetableDTO , Duration.ofDays(30));
            return timetableDTO;
        } else {
            return new TimetableDTO();
        }

    }
}
