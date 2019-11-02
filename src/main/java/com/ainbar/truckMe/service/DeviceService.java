package com.ainbar.truckMe.service;

import com.ainbar.truckMe.entities.Device;

import java.util.List;

public interface DeviceService {

    List<Device> listAll();

    Device addDevice(Device device);

    Device updateDevice(Device device);

    void deleteDevice(String code);
}
