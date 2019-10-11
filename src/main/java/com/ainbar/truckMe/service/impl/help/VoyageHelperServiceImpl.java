package com.ainbar.truckMe.service.impl.help;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.repo.RecordRepo;
import com.ainbar.truckMe.repo.VoyageRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class VoyageHelperServiceImpl {


    private static final int X = 1000;
    private static final int nbrRecords = 6;
    private static final int seuilA = 1200;
    private static final int seuilB = 1400;

    private RecordRepo recordRepo;
    private VoyageRepo voyageRepo;

    public VoyageHelperServiceImpl(RecordRepo recordRepo, VoyageRepo voyageRepo) {
        this.recordRepo = recordRepo;
        this.voyageRepo = voyageRepo;
    }

    public List<Voyage> saveVoyages() {
        List<Record> records = recordRepo.findAllBySavedFalseOrderByServertimeAsc();
        List<Voyage> list = new ArrayList<>();
        int j = 0;
        List<Record> list1 = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setSaved(true);
            double currentPoids = records.get(i).getPoids();
            if (currentPoids >= X && j < nbrRecords) {
                list1.add(records.get(i));
                j++;
            } else if (j >= nbrRecords && !list1.isEmpty()) {
                Record maxPoids = list1.stream().max(Comparator.comparing(Record::getPoids)).orElseThrow(NoSuchElementException::new);

                LocalDate date = maxPoids.getServertime().toLocalDateTime().toLocalDate();
                LocalTime time = maxPoids.getServertime().toLocalDateTime().toLocalTime();
                double sensorValue = maxPoids.getPoids();
                Integer camionId = maxPoids.getDeviceid();
                String coordonnees = maxPoids.getLatitude() + "," + maxPoids.getLongitude();

                Voyage voyage = new Voyage();

                voyage.setDate(date);
                voyage.setTime(time);
                voyage.setSensorValue(sensorValue);
                voyage.setIdCamion(camionId.longValue());
                voyage.setCoordonnees(coordonnees);

                long hours = ChronoUnit.HOURS.between(records.get(0).getDevicetime().toLocalDateTime(),
                        records.get(records.size() - 1).getDevicetime().toLocalDateTime());
                voyage.setHeureTravaillees(hours);
                voyage.setKmParcourue(records.get(records.size() - 1).getDistance() - records.get(0).getDistance());

                if (sensorValue < seuilA)
                    voyage.setCategoriePoids("A");
                else if (sensorValue > seuilA && sensorValue < seuilB)
                    voyage.setCategoriePoids("B");
                if (sensorValue > seuilB)
                    voyage.setCategoriePoids("C");

                voyage.setPoids(sensorValue * 50 / 1300);
                list.add(voyage);

                list1 = new ArrayList<>();

            } else if (j > nbrRecords && currentPoids > X) {
                j = 0;
                continue;
            }
            if (currentPoids < X && (list1.size() == nbrRecords || list1.isEmpty())) {
                if (i >= list.size() - 4) {
                    j = 0;
                    list1 = new ArrayList<>();
                } else if (i < list.size() - 4 && list.get(i + 1).getPoids() < X && list.get(i + 2).getPoids() < X && list.get(i + 3).getPoids() < X) {
                    j = 0;
                    list1 = new ArrayList<>();
                }

            }
        }
        recordRepo.saveAll(records);
        voyageRepo.saveAll(list);
        return list;
    }

    public List<Voyage> getVoyages() {
        return voyageRepo.findAll();
    }
}
