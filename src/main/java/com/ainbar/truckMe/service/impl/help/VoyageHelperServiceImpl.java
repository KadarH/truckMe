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


    private static final int X = 900;
    private static final int Y = 1200;
    private static final int Z = 1400;
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
        List<Record> records = recordRepo.findAllBySavedFalseOrderByDevicetimeAsc();
        List<Voyage> list = new ArrayList<>();
        int j = 0;
        List<Record> list1 = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getPoids() != null) {
                records.get(i).setSaved(true);
                System.out.println(i);
                double currentPoids = records.get(i).getPoids();
                if (currentPoids >= X && j < nbrRecords) {
                    list1.add(records.get(i));
                    j++;
                } else if (j >= nbrRecords && !list1.isEmpty()) {
                    Record maxPoids = list1.stream().max(Comparator.comparing(Record::getPoids)).orElseThrow(NoSuchElementException::new);

                    LocalDate date = maxPoids.getDevicetime().toLocalDateTime().toLocalDate();
                    LocalTime time = maxPoids.getDevicetime().toLocalDateTime().toLocalTime();
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
                    j = 0;
                    list1 = new ArrayList<>();
                }
                recordRepo.save(records.get(i));
            }
        }
        voyageRepo.saveAll(list);
        return list;
    }

    public List<Voyage> saveVoyages_new() {
        List<Record> records = recordRepo.findAllBySavedFalseOrderByDevicetimeAsc();
        List<Voyage> list = new ArrayList<>();
        int j = 0, k = 0, p = 0;
        List<Record> list1 = new ArrayList<>(), list2 = new ArrayList<>(), list3 = new ArrayList<>();
        Record maxA = null, maxB = null, maxC = null;
        boolean calculB = false;
        boolean calculC = false;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getPoids() != null) {
                if (records.get(i).getPoids() >= X) {
                    System.out.println("===>" + records.get(i).getPoids());
                    if (j < 5) {
                        list1.add(records.get(i));
                        j++;
                    } else if (j == 5) {
                        list1.add(records.get(i));
                        j++;
                        maxA = list1.stream().max(Comparator.comparing(Record::getPoids)).orElseThrow(NoSuchElementException::new);
                        System.out.println("maxA ========>" + maxA.getPoids());
                    } else if (records.get(i).getPoids() >= maxA.getPoids() || calculB) {
                        if (k < 5) {
                            calculB = true;
                            list2.add(records.get(i));
                            k++;
                        } else if (k == 5) {
                            list2.add(records.get(i));
                            k++;
                            maxB = list2.stream().max(Comparator.comparing(Record::getPoids)).orElseThrow(NoSuchElementException::new);
                            System.out.println("maxB ========>" + maxB.getPoids());
                        } else if (records.get(i).getPoids() >= maxB.getPoids() || calculC) {
                            if (p < 5) {
                                calculC = true;
                                list3.add(records.get(i));
                                p++;
                            } else if (p == 5) {
                                list3.add(records.get(i));
                                p++;
                                maxC = list3.stream().max(Comparator.comparing(Record::getPoids)).orElseThrow(NoSuchElementException::new);
                                System.out.println("maxC ========>" + maxC.getPoids());
                            }
                        }
                    }
                } else if (maxA != null && i < (records.size() - 4) &&
                        records.get(i + 1) != null && records.get(i + 1).getPoids() < X &&
                        records.get(i + 2) != null && records.get(i + 2).getPoids() < X
                ) {
                    System.out.println("======================");

                    LocalDate date = maxA.getDevicetime().toLocalDateTime().toLocalDate();
                    LocalTime time = maxA.getDevicetime().toLocalDateTime().toLocalTime();
                    double sensorValue = maxA.getPoids();
                    Integer camionId = maxA.getDeviceid();
                    String coordonnees = maxA.getLatitude() + "," + maxA.getLongitude();
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
                    voyage.setTypeVoyage(maxC != null ? "Complet" : "Incomplet");
                    list.add(voyage);
                    j = 0;
                    k = 0;
                    p = 0;
                    list1 = new ArrayList<>();
                    list2 = new ArrayList<>();
                    list3 = new ArrayList<>();
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
