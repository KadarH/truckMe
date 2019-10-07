package com.ainbar.truckMe.service;


import com.ainbar.truckMe.entities.Record;

import java.util.List;

public interface BatchService {

    List<Record> SaveRecordsFromTcPositions();
    List<Record> getRecords();

}
