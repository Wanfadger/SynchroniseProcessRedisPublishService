package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.ClockInTrail;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ClockInTrailRepository extends JpaRepository<ClockInTrail, String> {

    boolean existsByStaffCodeAndClockInDate(String staffCode , LocalDate clockInDate);
}
