package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.StaffDailyAttendanceSupervision;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.StaffDailyAttendanceTaskSupervision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffDailyAttendanceTaskSupervisionRepository extends JpaRepository<StaffDailyAttendanceTaskSupervision, String> {

    @Query("""
            SELECT SDATS FROM StaffDailyAttendanceTaskSupervision AS SDATS
            WHERE SDATS.status <> 8 AND SDATS.staffDailyAttendanceSupervision IN :termStaffDailyAttendanceSupervisions
            """)
    List<StaffDailyAttendanceTaskSupervision> allIn(List<StaffDailyAttendanceSupervision> termStaffDailyAttendanceSupervisions);
}