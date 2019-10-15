package com.ainbar.truckMe.service.impl.help;

import com.ainbar.truckMe.repo.TcPoisitionsRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BatchCleanerServiceImpl {

    private static final int X = 1000;

    private TcPoisitionsRepo tcPoisitionsRepo;

    public BatchCleanerServiceImpl(TcPoisitionsRepo tcPoisitionsRepo) {
        this.tcPoisitionsRepo = tcPoisitionsRepo;
    }

    public void cleanTcPositions() {
        tcPoisitionsRepo.deleteAll();
    }

}
