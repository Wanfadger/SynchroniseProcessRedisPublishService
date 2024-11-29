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

}
