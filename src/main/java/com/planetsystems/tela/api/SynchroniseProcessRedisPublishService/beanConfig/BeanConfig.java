package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.beanConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfig {
	@Bean
	@Primary
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}



}
