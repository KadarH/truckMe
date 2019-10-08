package com.ainbar.truckMe.service;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.TcPositions;
import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.repo.RecordRepo;
import com.ainbar.truckMe.repo.TcPoisitionsRepo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
public class BatchServiceImpl implements BatchService {

    private static final int X = 1000;
    private static final int nbrRecords = 6;

    private TcPoisitionsRepo tcPoisitionsRepo;
    private RecordRepo recordRepo;

    public BatchServiceImpl(TcPoisitionsRepo tcPoisitionsRepo, RecordRepo recordRepo) {
        this.tcPoisitionsRepo = tcPoisitionsRepo;
        this.recordRepo = recordRepo;
    }

    @Override
    public List<Record> SaveRecordsFromTcPositions() {
        List<TcPositions> list = tcPoisitionsRepo.findAll();
        List<Record> records = new ArrayList<>();
        Record record;
        for (TcPositions tcPositions : list) {
            record = new Record();
            record.setAttributes(tcPositions.getAttributes());
            record.setDeviceid(tcPositions.getDeviceid());
            record.setAltitude(tcPositions.getAltitude());
            record.setLatitude(tcPositions.getLatitude());
            record.setLongitude(tcPositions.getLongitude());
            record.setDevicetime(tcPositions.getDevicetime());
            record.setServertime(tcPositions.getServertime());
            record.setFixtime(tcPositions.getFixtime());

            JsonFactory factory = new JsonFactory();

            ObjectMapper mapper = new ObjectMapper(factory);
            JsonNode rootNode ;
            try {
                rootNode = mapper.readTree(tcPositions.getAttributes());

            }catch (Exception e){
                throw new RuntimeException(e);
            }

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            boolean ignition = false;
            int event = 2;
            int poids = 0;

            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                if (field.getKey().equals("event"))
                    event = field.getValue().asInt();
                if (field.getKey().equals("adc1"))
                    poids = field.getValue().asInt();
                if (field.getKey().equals("ignition"))
                    ignition = field.getValue().asBoolean();
            }

            if (event == 0 && !ignition) {
                record.setIgnition(ignition);
                record.setPoids((double) poids);
                records.add(record);
            }
        }
        records = recordRepo.saveAll(records);
        return records;
    }

    @Override
    public List<Voyage> calculVoyages() {
        List<Record> records = recordRepo.findAllByOrderByServertimeAsc();
        List<Voyage> list = new ArrayList<>();
        int j = 0;
        List<Record> list1 = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            double currentPoids = records.get(i).getPoids();
            if (currentPoids >= X && j < nbrRecords) {
                list1.add(records.get(i));
                j++;
            } else if (j >= nbrRecords && !list1.isEmpty()) {
                Record maxPoids = list1.stream().max(Comparator.comparing(Record::getPoids)).orElseThrow(NoSuchElementException::new);

                LocalDate date = maxPoids.getDevicetime().toLocalDateTime().toLocalDate();
                LocalTime time = maxPoids.getDevicetime().toLocalDateTime().toLocalTime();
                double poids = maxPoids.getPoids();
                Integer camionId = maxPoids.getDeviceid();
                String coordonnees = maxPoids.getLatitude() + "," + maxPoids.getLongitude();

                Voyage voyage = new Voyage();

                voyage.setDate(date);
                voyage.setTime(time);
                voyage.setPoids(poids);
                voyage.setIdCamion(camionId.longValue());
                voyage.setCoordonnees(coordonnees);
                list.add(voyage);
                list1 = new ArrayList<>();

            } else if (j > nbrRecords && currentPoids > X) {
                j = 0;
                continue;
            }
            if (currentPoids < X && (list1.size() == nbrRecords || list1.isEmpty())) {
                j = 0;
                list1 = new ArrayList<>();
            }
        }
        return list;
    }

    @Override
    public List<Record> getRecords() {
        return recordRepo.findAll();
    }

    @Scheduled(cron = "0 21 * * * ?")
    @Transactional
    public void run() {
        log.info("Batch processing ...");
        SaveRecordsFromTcPositions();
    }
}

