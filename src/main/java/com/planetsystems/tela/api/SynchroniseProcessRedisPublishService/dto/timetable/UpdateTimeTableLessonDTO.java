package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class UpdateTimeTableLessonDTO extends TimeTableLessonDTO{
    private String classId;
}
