package com.ainbar.truckMe;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.service.BatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/")
public class RecordController {

    private BatchService batchService;

    public RecordController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("records")
    public List<Record> getRecords(){
        return batchService.getRecords();
    }

    @GetMapping("voyages")
    public List<Voyage> calculVoyages() {
        return batchService.calculVoyages();
    }

}
