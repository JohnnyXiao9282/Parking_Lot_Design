package com.parking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parking_lots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Level> levels = new ArrayList<>();

    public void addLevel(Level level) {
        levels.add(level);
        level.setParkingLot(this);
    }

    public void removeLevel(Level level) {
        levels.remove(level);
        level.setParkingLot(null);
    }

    public int getTotalAvailableSpots() {
        return levels.stream()
                .mapToInt(Level::getAvailableSpots)
                .sum();
    }
}

