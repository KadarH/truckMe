package com.ainbar.truckMe.repo;

import com.ainbar.truckMe.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RecordRepo extends JpaRepository<Record, Long> {

    List<Record> findAllBySavedFalseOrderByDevicetimeAsc();

    List<Record> findAllByDeviceid(Integer id);

    List<Record> findAllByDeviceidAndDevicetimeLikeOrderByDevicetimeAsc(Integer idCamion, Timestamp date);
}
