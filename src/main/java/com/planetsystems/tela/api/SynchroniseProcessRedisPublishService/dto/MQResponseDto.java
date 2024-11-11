package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MQResponseDto<T> implements Serializable {
	ResponseType ResponseType;
	T data;
}
