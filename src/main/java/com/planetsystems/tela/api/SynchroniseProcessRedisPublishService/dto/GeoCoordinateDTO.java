package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GeoCoordinateDTO implements Serializable {
    private String id;
    double latitude;
    double longitude;
    boolean geoFenceActivated= true;
    boolean pinClockActivated = false;
    double maxDisplacement = 250.0;
}
