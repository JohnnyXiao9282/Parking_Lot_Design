package com.parking.repository;

import com.parking.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    List<Level> findByParkingLotId(Long parkingLotId);

    List<Level> findByIsSmallCarLevel(boolean isSmallCarLevel);
}

