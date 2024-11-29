package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.ClockIn;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository.projections.ClockInProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClockInRepository extends JpaRepository<ClockIn, String> {

    @Query(value = """
            SELECT CL FROM ClockIns AS CL
            WHERE CL.status <> 8
            AND CL.academicTerm.id =:termId AND CL.school.id =:schoolId
            """)
    List<ClockIn> allByTerm_School(String termId, String schoolId);

    @Query(value = """
            select cl."id" as id ,cl."clockInDate" as clockInDate,cl."clockInTime" as clockInTime ,cl."clockedStatus" as clockedStatus,cl."clockinType" as clockinType ,cl."comment" as comment,
            cl."createdDateTime" as createdDateTime,
            cl."displacement" as displacement ,cl."latitude" as latitude,cl."longitude" as longitude,cl."schoolStaff_id" as staffId,cl."status" as status 
            from  "ClockIns" as  cl where cl."status"<>8 and cl."academicTerm_id"=:termId and cl."school_id"=:schoolId
                        """, nativeQuery = true)
    List<ClockInProjection> nativeAllByTerm_School(String termId, String schoolId);


    @Query(value = """
            SELECT CL FROM ClockIns AS CL
            WHERE CL.status <> 8
            AND CL.clockInDate =:clockInDate AND CL.school.id =:schoolId
            """)
    List<ClockIn> allByDate_School(LocalDate clockInDate, String schoolId);

    @Query(value = """
            select cl."id" as id ,cl."clockInDate" as clockInDate,cl."clockInTime" as clockInTime ,cl."clockedStatus" as clockedStatus,cl."clockinType" as clockinType ,cl."comment" as comment,
            cl."createdDateTime" as createdDateTime,
            cl."displacement" as displacement ,cl."latitude" as latitude,cl."longitude" as longitude,cl."schoolStaff_id" as staffId,cl."status" as status 
            from "ClockIns" as  cl where cl."status"<>8 and cl."clockInDate"=:clockInDate and cl."school_id"=:schoolId
                        """, nativeQuery = true)
    List<ClockInProjection> nativeAllByDate_School(LocalDate clockInDate, String schoolId);

    @Query(value = """
            SELECT CL FROM ClockIns AS CL
            WHERE CL.status <> 8
            AND CL.clockInDate =:clockInDate AND CL.schoolStaff.id =:staffId
            """)
    Optional<ClockIn> clockInByDate_Staff(LocalDate clockInDate, String staffId);


    @Query(value = """
            SELECT CL FROM ClockIns AS CL
            WHERE CL.status <> 8
            AND CL.clockInDate =:clockInDate
            """)
    List<ClockIn> clockInByDate(LocalDate clockInDate);

}
