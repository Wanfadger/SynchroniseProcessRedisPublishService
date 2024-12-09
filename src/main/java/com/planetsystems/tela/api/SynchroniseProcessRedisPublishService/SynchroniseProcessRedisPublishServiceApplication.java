package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Slf4j
@RequiredArgsConstructor
@EnableCaching
public class SynchroniseProcessRedisPublishServiceApplication  {

	public static void main(String[] args) {
		SpringApplication.run(SynchroniseProcessRedisPublishServiceApplication.class, args);
	}

}
