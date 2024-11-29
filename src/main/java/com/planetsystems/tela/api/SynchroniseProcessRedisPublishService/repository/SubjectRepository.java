package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;


import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.Subject;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.SubjectClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
    List<Subject> findAllBySubjectClassificationNotNullAndStatusNotAndSubjectClassification(Status status , SubjectClassification subjectClassification);
}
