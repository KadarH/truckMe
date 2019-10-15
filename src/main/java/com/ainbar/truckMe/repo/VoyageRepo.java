package com.ainbar.truckMe.repo;

import com.ainbar.truckMe.entities.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoyageRepo extends JpaRepository<Voyage,Long> {

    List<Voyage> findAllByIdCamion(Long idCamion);
}
