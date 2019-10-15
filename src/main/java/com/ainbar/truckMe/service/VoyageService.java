package com.ainbar.truckMe.service;

import com.ainbar.truckMe.entities.Voyage;

import java.util.List;

public interface VoyageService {

    List<Voyage> getVoyagesByIdCamion(Long id);

    List<Voyage> getVoyagesByDevicetime(Long idCamion, String date);
}
