package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDTO implements Serializable {
    int localId;
    String id;
    String dob;
    String role;
    String gender;
    String lastName;
    String initials;
    boolean licensed;
    String firstName;
    String staffType;
    String onPayRoll;
    String nationalId;
    String phoneNumber;
    String emailAddress;
    int expectedHours;
    String employeeNumber;
    String hasSpecialNeeds;
}
