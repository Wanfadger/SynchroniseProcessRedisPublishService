package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.ClockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClockOutRepository extends JpaRepository<ClockOut, String> {

    @Query(value = """
            SELECT CO FROM ClockOuts AS CO
            JOIN FETCH CO.clockIn  AS CL
            WHERE CO.status <> 8 AND CL.status <> 8
            AND CO.clockOutDate =:clockInDate AND CL.schoolStaff.id =:staffId
            """)
    Optional<ClockOut> clockOutByDate_Staff(LocalDate clockInDate, String staffId);

    @Query(value = """
            SELECT CO FROM ClockOuts AS CO
            JOIN FETCH CO.clockIn  AS CL
            WHERE CO.status <> 8 AND CL.status <> 8
            AND CO.clockOutDate =:clockInDate AND CL.schoolStaff.id =:staffId
            """)
    List<ClockOut> allByDate_Staff(LocalDate clockInDate, String staffId);

    @Query(value = """
            SELECT CO FROM ClockOuts AS CO
            JOIN FETCH CO.clockIn AS CL
            WHERE CO.status <> 8 AND CL.status <> 8
            AND CL.academicTerm.id =:termId AND CL.school.id =:schoolId
            """)
    List<ClockOut> allByTerm_SchoolWithStaff(String termId, String schoolId);

    @Query(value = """
            SELECT CO FROM ClockOuts AS CO
            JOIN FETCH CO.clockIn AS CL
            WHERE CO.status <> 8 AND CL.status <> 8
            AND CL.clockInDate =:clockOutDate AND CL.school.id =:schoolId
            """)
    List<ClockOut> allByDate_SchoolWithStaff(LocalDate clockOutDate, String schoolId);


//    @EntityGraph(value = "clockOut-clockIn-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<ClockOut> findAllByStatusNotAndClockIn_AcademicTerm_IdAndClockIn_School_IdAndClockOutDateOrderByClockOutDateAsc(Status status, String academicTermId, String schoolId, LocalDate clockOutDate);
//
//    @EntityGraph(value = "clockOut-clockIn-graph", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<ClockOut> findByStatusNotAndClockIn_SchoolStaff_IdAndClockOutDate(Status status, String StaffId, LocalDate clockOutDate);
//
//    @EntityGraph(value = "clockOut-clockIn-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<ClockOut> findAllByStatusNotAndClockIn_SchoolStaff_IdAndClockOutDateBetween(Status status, String StaffId, LocalDate from, LocalDate to);
//
//    @EntityGraph(value = "clockOut-clockIn-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<ClockOut> findAllByStatusNotAndClockOutDate(Status status, LocalDate date);
//
//    Optional<ClockOut> findByStatusNotAndId(Status status, String clockOutId);
//
//    boolean existsByStatusNotAndClockOutDateAndClockIn_Id(Status status, LocalDate clockOutDate, String clockInId);
//
//
//    boolean existsByStatusNotAndClockIn_Id(Status status, String clockInId);
//
//    boolean existsByStatusNotAndClockIn_IdAndClockOutDate(Status status, String clockInId, LocalDate localDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id " +
//                            "from \"ClockOuts\" C \n" +
//                            "where \n" +
//                            "C.\"clockIn_id\"=:clockInId \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"clockOutDate\" =:date \n")
//    List<String> getStaffClockOutByDate(@Param("status") Status status, @Param("clockInId") String clockInId, @Param("date") LocalDate date);


}
