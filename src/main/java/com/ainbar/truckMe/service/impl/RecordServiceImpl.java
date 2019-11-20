package com.ainbar.truckMe.service.impl;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.repo.RecordRepo;
import com.ainbar.truckMe.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RecordServiceImpl implements RecordService {

    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private RecordRepo recordRepo;

    public RecordServiceImpl(RecordRepo recordRepo) {
        this.recordRepo = recordRepo;
    }

    @Override
    public List<Record> findAllByDeviceid(Integer id) {
        return recordRepo.findAllByDeviceid(id);
    }

    @Override
    public List<Record> getRecordByDeviceidAndDevicetime(Integer idCamion, String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(date + " 00:00", formatter);
        Timestamp deviceDate = Timestamp.valueOf(formatDateTime);
        return recordRepo.findAllByDeviceidAndDevicetimeBetweenOrderByDevicetimeAsc(idCamion, deviceDate, Timestamp.valueOf(deviceDate.toLocalDateTime().plusDays(1)));
    }
}
