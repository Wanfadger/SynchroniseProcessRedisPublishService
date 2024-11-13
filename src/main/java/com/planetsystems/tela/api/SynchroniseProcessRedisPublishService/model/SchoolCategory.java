package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


//school foundation body  i.e. community based, govt, catholic, COU
@Entity
@Table(name="SchoolCategories")
@NoArgsConstructor
@Setter
@Getter
public class SchoolCategory extends ParentEntity  {

    private String code;
    private String name;


    @OneToMany(mappedBy = "schoolCategory" , fetch = FetchType.LAZY)
    private Set<School> schools = new HashSet<School>();


    public SchoolCategory(String id) {
        super(id);
    }

}
