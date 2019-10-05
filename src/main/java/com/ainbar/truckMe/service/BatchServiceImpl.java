package com.ainbar.truckMe.service;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.TcPositions;
import com.ainbar.truckMe.repo.RecordRepo;
import com.ainbar.truckMe.repo.TcPoisitionsRepo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class BatchServiceImpl implements BatchService {

    private TcPoisitionsRepo tcPoisitionsRepo;
    private RecordRepo recordRepo;

    public BatchServiceImpl(TcPoisitionsRepo tcPoisitionsRepo, RecordRepo recordRepo) {
        this.tcPoisitionsRepo = tcPoisitionsRepo;
        this.recordRepo = recordRepo;
    }

    @Override
    public List<Record> getRecords(){
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
        return recordRepo.saveAll(records);

    }
}

