package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "Districts")
@Setter
@Getter
@NoArgsConstructor
public class District extends ParentEntity implements Serializable {

	private String code;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    @OneToMany(cascade=CascadeType.ALL , mappedBy = "district" , fetch = FetchType.LAZY)
    private Set<School> schools = new HashSet<>();

    private boolean rolledOut;

    public District(String id) {
        super(id);
    }


}
