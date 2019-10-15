package com.ainbar.truckMe.service.impl.help;

import com.ainbar.truckMe.entities.TcPositions;
import com.ainbar.truckMe.repo.TcPoisitionsRepo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public void cleanTcPositionsNotWorking() {
        List<TcPositions> list = tcPoisitionsRepo.findAll();
        for (TcPositions tcPositions : list) {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            JsonNode rootNode;
            try {
                rootNode = mapper.readTree(tcPositions.getAttributes());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            int poids = 0;
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                if (field.getKey().equals("adc1"))
                    poids = field.getValue().asInt();
            }
            if (poids < 650) tcPoisitionsRepo.delete(tcPositions);
        }
    }
}
