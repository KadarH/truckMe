package com.ainbar.truckMe.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Voyage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateFix;
    private LocalDateTime dateSys;
    private LocalDateTime dateDevice;
    private Double latitude;
    private Double longitude;
    private Double poids;
    private Boolean ignition;

    @ManyToOne
    private Device device;

}
