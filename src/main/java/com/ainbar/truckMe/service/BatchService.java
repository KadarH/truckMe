package com.ainbar.truckMe.service;


import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.Voyage;

import java.util.List;

public interface BatchService {

    List<Record> calculRecords();
    List<Record> getRecords();
    List<Voyage> calculVoyages();

    List<Voyage> getVoyages();
}
