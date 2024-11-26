package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.controllers;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.SynchronizeSchoolDataDTO;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto.SystemAppFeedBack;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.service.publisher.SynchronizeMobileDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/SynchroniseTelaData")
@RequiredArgsConstructor
@Slf4j
public class SynchroniseTelaDataController {

    final SynchronizeMobileDataService synchronizeMobileDataService;


    @Qualifier("halfHourCacheManager")
    final CacheManager cacheManager;

    @PostMapping
    public ResponseEntity<SystemAppFeedBack<Boolean>> synchronizeSchoolData(@RequestBody SynchronizeSchoolDataDTO dto){
        return synchronizeMobileDataService.synchronizeSchoolData(dto);
    }


    @GetMapping("evictDistricts")
    public String evictDistricts(){
        evictDistrictService();
        return "Successfully evicted";
    }

    public void evictDistrictService() {
        synchronizeMobileDataService.evictDistricts();
        // Optional.ofNullable(cacheManager.getCache(CacheKeys.DISTRICTS)).ifPresent((cache -> cache.evict(SimpleKey.EMPTY) ));
        log.info("CACHE EVICTING evictDistricts");
    }
}
