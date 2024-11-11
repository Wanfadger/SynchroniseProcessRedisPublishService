package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.LoginAudit;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository 
public interface LoginAuditRepository extends JpaRepository<LoginAudit , String> {
    List<LoginAudit> findAllByLoginTimeBetween(Date from , Date to);

    List<LoginAudit> findAllByStatusNot(Status status);
    List<LoginAudit> findAllByStatus(Status status);
    Optional<LoginAudit> findByStatusNotAndId(Status status , String id);
}
