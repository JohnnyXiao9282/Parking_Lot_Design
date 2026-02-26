package com.parking.repository;

import com.parking.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    List<ParkingSpot> findByLevelId(Long levelId);

    List<ParkingSpot> findByIsOccupied(boolean isOccupied);

    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.level.id = :levelId AND ps.isOccupied = false")
    List<ParkingSpot> findAvailableSpotsByLevel(Long levelId);

    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.isSmallCarSpot = :isSmallCarSpot AND ps.isOccupied = false")
    List<ParkingSpot> findAvailableSpotsByType(boolean isSmallCarSpot);
}

