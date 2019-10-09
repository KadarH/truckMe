package com.ainbar.truckMe.service.impl.help;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.TcPositions;
import com.ainbar.truckMe.repo.RecordRepo;
import com.ainbar.truckMe.repo.TcPoisitionsRepo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RecordHelperServiceImpl {


    private TcPoisitionsRepo tcPoisitionsRepo;
    private RecordRepo recordRepo;

    public RecordHelperServiceImpl(TcPoisitionsRepo tcPoisitionsRepo, RecordRepo recordRepo) {
        this.tcPoisitionsRepo = tcPoisitionsRepo;
        this.recordRepo = recordRepo;
    }

    public List<Record> saveRecords() {
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
            record.setSaved(false);
            JsonFactory factory = new JsonFactory();

            ObjectMapper mapper = new ObjectMapper(factory);
            JsonNode rootNode;
            try {
                rootNode = mapper.readTree(tcPositions.getAttributes());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            boolean ignition = false;
            int event = 2;
            int poids = 0;
            long distance = 0;

            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                if (field.getKey().equals("event"))
                    event = field.getValue().asInt();
                if (field.getKey().equals("adc1"))
                    poids = field.getValue().asInt();
                if (field.getKey().equals("ignition"))
                    ignition = field.getValue().asBoolean();
                if (field.getKey().equals("distance"))
                    distance = field.getValue().asLong();
            }

            if (event == 0 && !ignition) {
                record.setIgnition(ignition);
                record.setPoids((double) poids);
                record.setDistance(distance);
                records.add(record);
            }
        }
        records = recordRepo.saveAll(records);
        return records;
    }

    public List<Record> getRecords() {
        return recordRepo.findAll();
    }
}
