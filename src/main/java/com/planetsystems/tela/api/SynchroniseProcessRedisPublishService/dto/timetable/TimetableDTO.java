package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.timetable;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimetableDTO implements Serializable {
 private String id = "";
 private String academicTermId = "";
 private String schoolId = "";
 private List<ClassTimetableDTO> classTimetables = new ArrayList<>();
}
