package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LearnerHeadCountDTO implements Serializable {
    String classId;
    String learnerType;
    long totalFemale;
    long totalMale;
    String staffId;
    String submissionDate;
}

