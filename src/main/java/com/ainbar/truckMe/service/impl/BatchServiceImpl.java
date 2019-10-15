package com.ainbar.truckMe.service.impl;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.service.BatchService;
import com.ainbar.truckMe.service.impl.help.BatchCleanerServiceImpl;
import com.ainbar.truckMe.service.impl.help.RecordHelperServiceImpl;
import com.ainbar.truckMe.service.impl.help.VoyageHelperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class BatchServiceImpl implements BatchService {

    private RecordHelperServiceImpl recordHelperService;
    private VoyageHelperServiceImpl voyageHelperService;
    private BatchCleanerServiceImpl batchCleanerService;

    public BatchServiceImpl(RecordHelperServiceImpl recordHelperService, VoyageHelperServiceImpl voyageHelperService, BatchCleanerServiceImpl batchCleanerService) {
        this.recordHelperService = recordHelperService;
        this.voyageHelperService = voyageHelperService;
        this.batchCleanerService = batchCleanerService;
    }

    @Override
    public List<Record> calculRecords() {
        return this.recordHelperService.saveRecords();
    }

    @Override
    public List<Voyage> calculVoyages() {
        return this.voyageHelperService.saveVoyages_new();
    }

    @Override
    public List<Record> getRecords() {
        return recordHelperService.getRecords();
    }

    @Override
    public List<Voyage> getVoyages() {
        return voyageHelperService.getVoyages();
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void calculRecordsBatch() {
        log.info("Batch processing for calcul Records ...");
        calculRecords();
    }

    @Scheduled(cron = "0 10 17 * * ?")
    public void calculVoyageBatch() {
        log.info("Batch processing for calcul Voyage ...");
        calculVoyages();
    }

    @Scheduled(cron = "0 20 17 * * ?")
    public void cleanerBatch() {
        log.info("Batch processing for cleaning Database ...");
        batchCleanerService.cleanTcPositions();
        batchCleanerService.cleanRecordTable();
    }
}