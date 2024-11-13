package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "SchoolGeoCoordinates")
@Setter
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(name = "school", columnNames = {"school_id"})})
public class SchoolGeoCoordinate extends ParentEntity {

    @ManyToOne(fetch = FetchType.LAZY , targetEntity = School.class)
    private School school;
    private double longtitude;
    private double latitude;
    private boolean geoFenceActivated=true;
    private boolean pinClockActivated=false;
    private int displacement=250; //100M

}
