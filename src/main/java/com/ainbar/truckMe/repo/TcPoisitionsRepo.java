package com.ainbar.truckMe.repo;

import com.ainbar.truckMe.entities.TcPositions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcPoisitionsRepo extends JpaRepository<TcPositions, Long> {
}
