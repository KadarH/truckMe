package com.ainbar.truckMe.service.impl.help;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.repo.RecordRepo;
import com.ainbar.truckMe.repo.VoyageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
@Slf4j
public class VoyageHelperServiceImpl {

    private static final int SEUIL = 900;
    private static final int MAX_RECORDS = 5;

    private RecordRepo recordRepo;
    private VoyageRepo voyageRepo;

    public VoyageHelperServiceImpl(RecordRepo recordRepo, VoyageRepo voyageRepo) {
        this.recordRepo = recordRepo;
        this.voyageRepo = voyageRepo;
    }

    public List<Voyage> saveVoyages() {
        List<Record> records = recordRepo.findAllBySavedFalseOrderByDevicetimeAsc();
        List<Voyage> list = new ArrayList<>();
        int j = 0, k = 0, p = 0;
        List<Record> list1 = new ArrayList<>();
        Record maxA = null, maxB = null, maxC = null;
        boolean calculB = false;
        boolean calculC = false;
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            if (record.getPoids() != null) {
                if (record.getPoids() >= SEUIL) {
                    if (j < MAX_RECORDS) {
                        list1.add(record);
                        j++;
                    } else if (j == MAX_RECORDS) {
                        list1.add(record);
                        j++;
                        maxA = list1.stream().max(Comparator.comparing(Record::getPoids)).orElseThrow(NoSuchElementException::new);
                        list1 = new ArrayList<>();
                    } else if (maxA != null && (record.getPoids() >= maxA.getPoids() || calculB)) {
                        if (k < MAX_RECORDS) {
                            calculB = true;
                            list1.add(record);
                            k++;
                        } else if (k == MAX_RECORDS) {
                            list1.add(record);
                            k++;
                            maxB = list1.stream().max(Comparator.comparing(Record::getPoids)).orElseThrow(NoSuchElementException::new);
                            list1 = new ArrayList<>();
                        } else if (maxB != null && (record.getPoids() >= maxB.getPoids() || calculC)) {
                            if (p < MAX_RECORDS) {
                                calculC = true;
                                list1.add(record);
                                p++;
                            } else if (p == MAX_RECORDS) {
                                list1.add(record);
                                p++;
                                maxC = list1.stream().max(Comparator.comparing(Record::getPoids)).orElseThrow(NoSuchElementException::new);
                                list1 = new ArrayList<>();
                            }
                        }
                    }
                } else if (maxA != null && i < (records.size() - 4) &&
                        records.get(i + 1) != null && records.get(i + 1).getPoids() < SEUIL &&
                        records.get(i + 2) != null && records.get(i + 2).getPoids() < SEUIL &&
                        records.get(i + 3) != null && records.get(i + 3).getPoids() < SEUIL
                ) {
                    LocalDate date = maxA.getDevicetime().toLocalDateTime().toLocalDate();
                    LocalTime time = maxA.getDevicetime().toLocalDateTime().toLocalTime();
                    Integer camionId = maxA.getDeviceid();
                    String coordonnees = maxA.getLatitude() + "," + maxA.getLongitude();
                    double sensorValue = maxA.getPoids();
                    long hours = ChronoUnit.HOURS.between(records.get(0).getDevicetime().toLocalDateTime(),
                            records.get(records.size() - 1).getDevicetime().toLocalDateTime());

                    Voyage voyage = new Voyage();
                    voyage.setDate(date);
                    voyage.setTime(time);
                    voyage.setSensorValue(sensorValue);
                    voyage.setIdCamion(camionId.longValue());
                    voyage.setCoordonnees(coordonnees);
                    voyage.setHeureTravaillees(hours);
                    voyage.setKmParcourue(records.get(records.size() - 1).getDistance() - records.get(0).getDistance());
                    voyage.setPoids(sensorValue * 50 / 1300);
                    voyage.setMaxA(maxA.getPoids());
                    voyage.setMaxB(Optional.ofNullable(maxB).map(Record::getPoids).orElse(null));
                    voyage.setMaxC(Optional.ofNullable(maxC).map(Record::getPoids).orElse(null));

                    if (maxC != null) voyage.setTypeVoyage("C");
                    else if (maxB != null) voyage.setTypeVoyage("B");
                    else voyage.setTypeVoyage("A");

                    list.add(voyage);

                    log.info("Voyage Added : " + voyage);
                    list1 = new ArrayList<>();
                    j = 0;
                    k = 0;
                    p = 0;
                    maxA = null;
                    maxB = null;
                    maxC = null;
                    calculB = false;
                    calculC = false;
                }

            }
        }
        voyageRepo.saveAll(list);
        return list;
    }

    public List<Voyage> getVoyages() {
        return voyageRepo.findAll();
    }
}
