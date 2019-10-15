package com.ainbar.truckMe.controller;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.service.BatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/")
public class IndexController {

    private BatchService batchService;

    public IndexController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("records")
    public List<Record> getRecords() {
        return batchService.getRecords();
    }

    @GetMapping("voyages")
    public List<Voyage> getVoyages() {
        return batchService.getVoyages();
    }

    @GetMapping("records/batch")
    public List<Record> calculRecords() {
        return batchService.calculRecords();
    }

    @GetMapping("voyages/batch")
    public List<Voyage> calculVoyages() {
        return batchService.calculVoyages();
    }

}
