package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StaffDailyTimetableDTO implements Serializable {
    String id;
    String subjectId;
    String classId;
    String staffId;
    String timeAttendanceId;
    String actionStatus;
    String comment;
    String submissionDate;
    String startTime;
    String endTime;
}
