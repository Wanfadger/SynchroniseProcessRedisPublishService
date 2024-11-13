package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.GeneralUserDetail;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneralUserDetailsRepository extends JpaRepository<GeneralUserDetail, String> {

    List<GeneralUserDetail> findAllByStatusNot(Status status);

    Optional<GeneralUserDetail> findByStatusNotAndEmailIgnoreCase(Status status, String email);

    @Query("""
            SELECT G FROM GeneralUserDetails  G
            JOIN FETCH G.schoolStaff ST
            JOIN FETCH ST.school S
            JOIN FETCH S.district D
            WHERE G.status <> :status AND ST.status <> :status AND S.status <> :status AND D.status <> :status
            AND G.email = :email
            """)
    Optional<GeneralUserDetail> findByStatusNotAndEmailIgnoreCaseWithStaff_School(Status status, String email);

    Optional<GeneralUserDetail> findByStatusNotAndId(Status status, String id);

}
