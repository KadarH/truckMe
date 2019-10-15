package com.ainbar.truckMe.controller;


import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.service.BatchService;
import com.ainbar.truckMe.service.VoyageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/voyages")
public class VoyagesController {

    private BatchService batchService;
    private VoyageService voyageService;

    public VoyagesController(BatchService batchService, VoyageService voyageService) {
        this.batchService = batchService;
        this.voyageService = voyageService;
    }

    @GetMapping("/{idCamion}")
    public List<Voyage> getVoyagesByIdDevice(@PathVariable Long idCamion) {
        return voyageService.getVoyagesByIdCamion(idCamion);
    }

    @GetMapping("/{idCamion}/{date}")
    public List<Voyage> getVoyagesByIdDevice(@PathVariable Long idCamion, @PathVariable String date) {
        return voyageService.getVoyagesByDevicetime(idCamion, date);
    }

    @GetMapping("/all")
    public List<Voyage> getVoyages() {
        return batchService.getVoyages();
    }

    @GetMapping("/batch")
    public List<Voyage> calculVoyages() {
        return batchService.calculVoyages();
    }

}