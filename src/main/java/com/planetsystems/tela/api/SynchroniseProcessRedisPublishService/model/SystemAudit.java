package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemAudit {
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Id
    private String id;
    private String resource;
    private String email;
    private String requestType;


    @Column(length = 1024)
    private String requestDetails;

    @CreationTimestamp()
    private LocalDateTime createdDateTime;



    public SystemAudit(String resource, String email, String requestType, String requestDetails) {
        this.resource = resource;
        this.email = email;
        this.requestType = requestType;
        this.requestDetails = requestDetails;
    }
}
