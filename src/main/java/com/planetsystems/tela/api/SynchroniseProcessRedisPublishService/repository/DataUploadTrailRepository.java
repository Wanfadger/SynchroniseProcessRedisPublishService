package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.DataUploadTrail;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataUploadTrailRepository extends JpaRepository<DataUploadTrail, String> {
    List<DataUploadTrail> findAllByStatusNot(Status status);
    Optional<DataUploadTrail> findByStatusNotAndId(Status status , String id);
}
