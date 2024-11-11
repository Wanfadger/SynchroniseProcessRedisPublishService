package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.SchoolOwner;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolOwnerRepository extends JpaRepository<SchoolOwner, String> {

    @Query("""
            SELECT SO FROM SchoolOwner SO 
            JOIN FETCH SO.school AS SCH
            JOIN FETCH SO.userGroup G
            """)
    List<SchoolOwner> findAllWithGroup_School();

    Optional<SchoolOwner> findByStatusNotAndEmail(Status status , String email);

    @Query("""
            SELECT SO FROM SchoolOwner SO 
            JOIN FETCH SO.school AS SCH
            JOIN FETCH SO.userGroup AS  G
            JOIN FETCH SCH.district AS D
            WHERE SO.email =:email
            """)
    Optional<SchoolOwner> findWithGroup_School_DistrictByUsername(String email);


}
