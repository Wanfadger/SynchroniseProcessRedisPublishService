package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ClassDTO implements Serializable {
    private String id;
    private String name;
    private int localId;
    private String parentSchoolClassId;
}
