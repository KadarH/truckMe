package com.ainbar.truckMe.service;

import com.ainbar.truckMe.entities.Record;

import java.util.List;

public interface RecordService {

    List<Record> findAllByDeviceid(Integer id);

    List<Record> getRecordByDeviceidAndDevicetime(Integer idCamion, String date);
}
