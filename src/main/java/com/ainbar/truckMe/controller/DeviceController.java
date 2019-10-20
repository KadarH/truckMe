package com.ainbar.truckMe.controller;


import com.ainbar.truckMe.entities.Device;
import com.ainbar.truckMe.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@Slf4j
public class DeviceController {

    private DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("")
    public List<Device> listAll() {
        return deviceService.listAll();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = "add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Device addDevice(@RequestBody Device device) {
        log.info("Add new Device : " + device.toString());

        return deviceService.addDevice(device);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = "edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Device editDevice(@RequestBody Device device) {
        log.info("Edit Device : " + device.toString());

        return deviceService.updateDevice(device);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "delete/{code}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteDevice(@RequestParam String code) {
        log.info("Delete device with code = " + code);
        deviceService.deleteDevice(code);
    }
}
