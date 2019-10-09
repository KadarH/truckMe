package com.ainbar.truckMe.repo;

import com.ainbar.truckMe.entities.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoyageRepo extends JpaRepository<Voyage,Long> {
}
