package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class SchoolOwner extends ParentEntity{ ;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY , targetEntity = SystemUserGroup.class)
    @JoinColumn(nullable = false)
    private SystemUserGroup userGroup;

    @ManyToOne(fetch = FetchType.LAZY , targetEntity = School.class)
    @JoinColumn(nullable = false)
    private School school;
}
