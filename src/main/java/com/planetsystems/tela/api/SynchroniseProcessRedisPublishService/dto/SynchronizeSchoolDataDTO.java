package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;


import java.io.Serializable;

public record SynchronizeSchoolDataDTO(String telaSchoolNumber , String date) implements Serializable {
}
