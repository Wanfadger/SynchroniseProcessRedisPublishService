package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.SchoolGeoCoordinate;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SchoolGeoCoordinateRepository extends JpaRepository<SchoolGeoCoordinate, String> {


    Optional<SchoolGeoCoordinate> findByStatusNotAndSchool_Id(Status status,String schoolId);

    List<SchoolGeoCoordinate> findAllByStatusNotAndSchool_District_IdAndAndSchool_SchoolOwnership(Status status, String districtId, SchoolOwnership schoolOwnership);
}
