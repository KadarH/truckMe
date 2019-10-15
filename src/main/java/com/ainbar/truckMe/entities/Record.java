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

    private Integer deviceid;
    private Timestamp devicetime;
    private Boolean saved = false;
    private double latitude;
    private double longitude;
    @JsonIgnore
    private boolean valid;

    private Double poids;

    @JsonIgnore
    private String protocol;

    @JsonIgnore
    private float altitude;

    @JsonIgnore
    private float speed;

    @JsonIgnore
    private float course;

    @JsonIgnore
    private String address;

    @JsonIgnore
    private Boolean ignition;

    @JsonIgnore
    private double accuracy;

    @JsonIgnore
    private String network;

    @JsonIgnore
    private long distance;
}
