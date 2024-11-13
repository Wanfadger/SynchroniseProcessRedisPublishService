package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AcademicTermDTO implements Serializable {
    private String id;
    private String name;
    private String startDate;
    private String endDate;
    private String year;
}
