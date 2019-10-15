package com.ainbar.truckMe.controller;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.service.BatchService;
import com.ainbar.truckMe.service.RecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {

    private BatchService batchService;
    private RecordService recordService;

    public RecordController(BatchService batchService, RecordService recordService) {
        this.batchService = batchService;
        this.recordService = recordService;
    }

    @GetMapping("/{id}")
    public List<Record> getVoyagesByIdDevice(@PathVariable Integer id) {
        return recordService.findAllByDeviceid(id);
    }

    @GetMapping("/{idCamion}/{date}")
    public List<Record> getRecordsByIdDeviceAndDate(@PathVariable Integer idCamion, @PathVariable String date) {
        return recordService.getRecordByDeviceidAndDevicetime(idCamion, date);
    }

    @GetMapping("/all")
    public List<Record> getRecords() {
        return batchService.getRecords();
    }

    @GetMapping("/batch")
    public List<Record> calculRecords() {
        return batchService.calculRecords();
    }
}
