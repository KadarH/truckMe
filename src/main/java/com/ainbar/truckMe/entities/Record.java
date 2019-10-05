package com.ainbar.truckMe.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String protocol;
    private Integer deviceid;
    private Timestamp servertime;
    private Timestamp devicetime;
    private Timestamp fixtime;
    private boolean valid;
    private double latitude;
    private double longitude;
    private float altitude;
    private float speed;
    private float course;
    private String address;
    private String attributes;

    private Boolean ignition;
    private Double poids;
    private double accuracy;
    private String network;
}
