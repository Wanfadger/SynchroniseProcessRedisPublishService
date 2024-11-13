package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.controllers;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.SynchronizeSchoolDataDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.SystemAppFeedBack;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.publisher.SynchronizeMobileDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SynchroniseTelaData")
@RequiredArgsConstructor
public class SynchroniseTelaDataController {

    final SynchronizeMobileDataService synchronizeMobileDataService;

    @PostMapping
    public ResponseEntity<SystemAppFeedBack<Boolean>> synchronizeSchoolData(@RequestBody SynchronizeSchoolDataDTO dto){
        return synchronizeMobileDataService.synchronizeSchoolData(dto);
    }
}
