package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class LearnerAttendanceDTO implements Serializable {
    String classId;
    String staffId;
    String comment;
    long maleAbsent;
    long malePresent;
    long femaleAbsent;
    long femalePresent;
    String submissionDate;
    String learnerType;
}


