package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SchoolDTO implements Serializable {
    private String telaSchoolNumber;
    private String phoneNumber;
    private String name;
    private IdNameDTO district;
    private AcademicTermDTO academicTerm;
    private String regNo;
    private String schoolLevel;
    private String schoolOwnership;
    private String schoolType;
    private boolean licensed;
    private String licenseKey;
    private GeoCoordinateDTO geoCoordinate;



}
