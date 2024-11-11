package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.SchoolMaterial;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SchoolMaterialRepository extends JpaRepository<SchoolMaterial,String> {

    boolean existsByStatusNotAndMaterialNameIgnoreCase(Status status  ,String name);
    List<SchoolMaterial> findAllByStatusNot(Status status);
    List<SchoolMaterial> findAllByStatus(Status status);
    Optional<SchoolMaterial> findByStatusNotAndId(Status status , String id);



}
