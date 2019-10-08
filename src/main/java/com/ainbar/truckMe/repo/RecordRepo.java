package com.ainbar.truckMe.repo;

import com.ainbar.truckMe.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepo extends JpaRepository<Record, Long> {

    List<Record> findAllByOrderByServertimeAsc();
}
