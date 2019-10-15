package com.ainbar.truckMe.service.impl;

import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.repo.VoyageRepo;
import com.ainbar.truckMe.service.VoyageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Slf4j
public class VoyageServiceImpl implements VoyageService {

    private VoyageRepo voyageRepo;

    public VoyageServiceImpl(VoyageRepo voyageRepo) {
        this.voyageRepo = voyageRepo;
    }

    @Override
    public List<Voyage> getVoyagesByIdCamion(Long id) {
        return voyageRepo.findAllByIdCamion(id);
    }

    @Override
    public List<Voyage> getVoyagesByDevicetime(Long idCamion, String date) {
        return voyageRepo.findAllByIdCamionAndDate(idCamion, LocalDate.parse(date));
    }
}
