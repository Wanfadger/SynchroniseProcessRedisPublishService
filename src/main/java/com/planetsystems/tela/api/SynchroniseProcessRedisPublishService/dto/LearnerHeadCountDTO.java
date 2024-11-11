package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LearnerHeadCountDTO {
    String classId;
    String learnerType;
    long totalFemale;
    long totalMale;
    String staffId;
    String submissionDate;
}

