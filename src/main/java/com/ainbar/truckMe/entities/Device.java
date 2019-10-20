package com.ainbar.truckMe.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String groupe;
    private String description;

    @Column(unique = true)
    private String code;
}
