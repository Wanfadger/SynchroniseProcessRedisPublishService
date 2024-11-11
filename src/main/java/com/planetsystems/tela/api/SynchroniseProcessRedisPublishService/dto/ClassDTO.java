package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ClassDTO {
    private String id;
    private String name;
    private int localId;
    private String parentSchoolClassId;
}
