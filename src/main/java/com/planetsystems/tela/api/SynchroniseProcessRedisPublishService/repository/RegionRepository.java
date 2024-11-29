package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.Region;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
	
    boolean existsByStatusNotAndNameIgnoreCase(Status status , String name);
}
