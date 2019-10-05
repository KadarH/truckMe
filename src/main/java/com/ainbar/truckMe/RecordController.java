package com.ainbar.truckMe;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.service.BatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/records")
public class RecordController {

    private BatchService batchService;

    public RecordController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("")
    public List<Record> getRecords(){
        return batchService.getRecords();
    }

}
