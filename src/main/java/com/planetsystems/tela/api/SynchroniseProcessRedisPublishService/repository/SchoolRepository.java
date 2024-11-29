package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.School;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository.projections.IdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, String> {

    @Query(value = """
                 SELECT S FROM School AS S
                 JOIN FETCH S.district
                 LEFT JOIN S.schoolGeoCoordinateList AS SG
                 WHERE S.status <> :status AND S.id= :id
            """)
    Optional<School> findByStatusNotAndId(Status status, String id);

    Optional<IdProjection> findByTelaSchoolUIDAndStatusNot(String telaSchoolUID, Status status);

    @Query(value = """
                 SELECT S FROM School AS S
                 JOIN FETCH S.district
                 LEFT JOIN S.schoolGeoCoordinateList AS SG
                 WHERE S.status <> :status AND S.telaSchoolUID = :telaNumber OR S.deviceNumber = :deviceNumber
            """)
    Optional<School> byTelaNumberOrDeviceNumber(Status status, String telaNumber , String deviceNumber);


    @Query("""
             SELECT S.id AS id FROM School  AS S WHERE 
             S.status <> :status AND S.telaSchoolUID = :telaSchoolUID
             """)
    Optional<IdProjection> idByStatusAndTelaSchoolUID(Status status , String telaSchoolUID);

}
