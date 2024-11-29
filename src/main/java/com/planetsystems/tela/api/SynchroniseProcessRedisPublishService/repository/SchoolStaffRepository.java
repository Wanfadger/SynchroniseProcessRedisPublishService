package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.SchoolStaff;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolStaffRepository extends JpaRepository<SchoolStaff, String> {
    @Query(value = "SELECT ST FROM SchoolStaff ST " +
            "JOIN FETCH ST.generalUserDetail G " +
            "JOIN FETCH ST.school S " +
            "WHERE ST.status <>:status AND G.status <>:status AND ST.staffInServiceStatus = 0 AND S.id =:schoolId")
    List<SchoolStaff> findAllBySchoolWithSchool_StaffDetail(Status status , String schoolId);

}
