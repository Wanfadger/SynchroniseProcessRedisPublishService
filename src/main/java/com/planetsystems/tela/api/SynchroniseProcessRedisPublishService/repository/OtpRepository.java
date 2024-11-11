package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.Otp;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OtpRepository extends JpaRepository<Otp , String> {
    Optional<Otp> findByUsername(String username);
    Optional<Otp> findByUsernameAndCode(String username , int otp);
    boolean existsByCode(int otp);
    boolean existsByUsernameAndCode(String username , int otp);
}
