package com.ainbar.truckMe.service.impl.help;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.repo.RecordRepo;
import com.ainbar.truckMe.repo.TcPoisitionsRepo;
import com.ainbar.truckMe.repo.VoyageRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BatchCleanerServiceImpl {

    private static final int X = 1000;

    private TcPoisitionsRepo tcPoisitionsRepo;
    private RecordRepo recordRepo;
    private VoyageRepo voyageRepo;

    public BatchCleanerServiceImpl(TcPoisitionsRepo tcPoisitionsRepo, RecordRepo recordRepo, VoyageRepo voyageRepo) {
        this.tcPoisitionsRepo = tcPoisitionsRepo;
        this.recordRepo = recordRepo;
        this.voyageRepo = voyageRepo;
    }

    public void cleanTcPositions() {
        tcPoisitionsRepo.deleteAll();
    }

    public void cleanRecordTable() {
        List<Record> list = recordRepo.findAllByPoidsSmallThanX(X);
        recordRepo.deleteAll(list);
    }

}
