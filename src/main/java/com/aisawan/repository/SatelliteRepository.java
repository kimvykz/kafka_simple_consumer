package com.aisawan.repository;

import com.aisawan.model.SatelliteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatelliteRepository extends JpaRepository<SatelliteEntity, Integer> {

}
