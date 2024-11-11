package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.SchoolClass;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, String> {
//        List<SchoolClass> findAllByStatusNot(Status status);
//    List<SchoolClass> findAllByStatus(Status status);
//
//    Optional<SchoolClass> findByStatusNotAndId(Status status , String id);
//
//
//    boolean existsByStatusNotAndNameIgnoreCaseAndAcademicTerm_IdAndSchool_Id(Status status ,String name, String termId , String schoolId);
//
//    Optional<SchoolClass> findByStatusNotAndNameIgnoreCaseAndAcademicTerm_IdAndSchool_Id(Status status ,String name, String termId , String schoolId);
//
//    Optional<SchoolClass> findByStatusNotAndAcademicTerm_IdAndSchool_Id(Status status , String termId , String schoolId);
//
//    List<SchoolClass> findAllByStatusNotAndAcademicTerm_StatusNotAndSchool_StatusNot(Status schoolClassStatus  ,Status termStatus  ,Status schoolStatus);
//
//    List<SchoolClass> findAllByStatusNotAndSchool_Id(Status schoolClassStatus  ,String schoolId);
//    long countAllByStatusNotAndSchool_Id(Status deleted, String schoolId);
//    long countAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status deleted, String academicTerm , String schoolId);
//
//    List<SchoolClass> findAllByStatusNotAndAcademicTerm_Id(Status status  ,String termId);
//
    List<SchoolClass> findAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status schoolClassStatus  ,String termId , String schoolId);
//
//
//    @Query("SELECT S FROM  School  S " +
//            "" +
//            "")
//   List<SchoolClass> dbFindAllBySchoolWithLearnerEnrollment(Status status , String schoolId);

}
