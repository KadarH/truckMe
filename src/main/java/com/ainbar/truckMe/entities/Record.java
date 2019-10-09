package com.ainbar.truckMe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @JsonIgnore
    private String protocol;
    private Integer deviceid;
    private Timestamp servertime;

    @JsonIgnore
    private Timestamp devicetime;

    @JsonIgnore
    private Timestamp fixtime;

    @JsonIgnore
    private boolean valid;
    private double latitude;
    private double longitude;

    @JsonIgnore
    private float altitude;

    @JsonIgnore
    private float speed;

    @JsonIgnore
    private float course;
    private String address;
    @JsonIgnore
    private String attributes;
    private Boolean ignition;
    private Double poids;
    private double accuracy;

    @JsonIgnore
    private String network;
    private long distance;
    private Boolean saved = false;
}
