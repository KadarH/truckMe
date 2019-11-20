package com.ainbar.truckMe.service.impl;

import com.ainbar.truckMe.entities.Device;
import com.ainbar.truckMe.repo.DeviceRepo;
import com.ainbar.truckMe.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private DeviceRepo deviceRepo;

    public DeviceServiceImpl(DeviceRepo deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    @Override
    public List<Device> listAll() {
        return deviceRepo.findAll();
    }

    @Override
    public Device addDevice(Device device) {
        return deviceRepo.save(device);
    }

    @Override
    public Device updateDevice(Device device) {
        Device old = deviceRepo.findById(device.getId()).get();
        if (device.getName() != null && !device.getName().isEmpty())
            old.setName(device.getName());
        if (device.getGroupe() != null && !device.getGroupe().isEmpty())
            old.setGroupe(device.getGroupe());
        if (device.getDescription() != null && !device.getDescription().isEmpty())
            old.setDescription(device.getDescription());
        if (device.getCode() != null && !device.getCode().isEmpty())
            old.setCode(device.getCode());
        return old;
    }

    @Override
    public void deleteDevice(String code) {
        deviceRepo.deleteByCode(code);
    }
}
