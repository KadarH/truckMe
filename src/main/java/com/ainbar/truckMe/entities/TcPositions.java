package com.ainbar.truckMe.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "tc_positions")
public class TcPositions {

    @Id
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
    private double accuracy;
    private String network;

}
