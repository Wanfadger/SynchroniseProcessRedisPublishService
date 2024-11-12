package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.supervision;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDailyAttendanceTaskSupervisionDTO implements Serializable {
    String id;
    String lessonTaskId;
    String timeAttendanceId;
    String supervisorId;
//    String supervisionStatus; /// removed
    String supervisionDate;
    String supervisionTime;
    String timeStatus;// taught or not taught
    String supervisionComment;
}
