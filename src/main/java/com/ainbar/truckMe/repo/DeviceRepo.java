package com.ainbar.truckMe.repo;

import com.ainbar.truckMe.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Long> {
    void deleteByCode(String code);
}
