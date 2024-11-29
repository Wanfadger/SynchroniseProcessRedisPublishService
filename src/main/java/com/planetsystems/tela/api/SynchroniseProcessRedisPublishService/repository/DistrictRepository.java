package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.District;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {

    @EntityGraph(attributePaths = {"region"})
    List<District> findAllByStatusNot(Status status);
}
