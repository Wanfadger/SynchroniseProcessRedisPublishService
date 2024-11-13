package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class TimeTableLessonDTO implements Serializable {
    private String id;
    private String startTime;
    private String endTime;
    private String lessonDay;
    private String subjectId;
    private String staffId;
}
