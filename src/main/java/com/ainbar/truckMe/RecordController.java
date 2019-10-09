package com.ainbar.truckMe;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.service.BatchService;
import com.ainbar.truckMe.service.impl.help.BatchCleanerServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/")
public class RecordController {

    private BatchService batchService;
    private BatchCleanerServiceImpl batchCleanerService;

    public RecordController(BatchService batchService, BatchCleanerServiceImpl batchCleanerService) {
        this.batchService = batchService;
        this.batchCleanerService = batchCleanerService;
    }

    @GetMapping("records")
    public List<Record> getRecords(){
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

    @GetMapping("records/clean")
    public String cleanRecords() {
        batchCleanerService.cleanRecordTable();
        return "Record Table Cleaned Successfuly..";

    }

}
