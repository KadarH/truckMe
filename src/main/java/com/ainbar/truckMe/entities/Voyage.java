package com.ainbar.truckMe.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Voyage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime time;
    private Double sensorValue;
    private Double poids;
    private String typeVoyage;
    private Long idCamion;
    private String categoriePoids;
    private String coordonnees;
    private Double maxA;
    private Double maxB;
    private Double maxC;

}
