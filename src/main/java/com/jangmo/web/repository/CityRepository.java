package com.jangmo.web.repository;

import com.jangmo.web.model.entity.administrative.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    City findByName(String cityName);
}
