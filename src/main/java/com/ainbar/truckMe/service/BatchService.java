package com.ainbar.truckMe.service;


import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.Voyage;

import java.util.List;

public interface BatchService {

    List<Record> SaveRecordsFromTcPositions();
    List<Record> getRecords();

    List<Voyage> calculVoyages();

}
