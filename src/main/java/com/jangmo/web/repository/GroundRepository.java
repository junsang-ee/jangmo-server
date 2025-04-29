package com.jangmo.web.repository;

import com.jangmo.web.model.entity.GroundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroundRepository extends JpaRepository<GroundEntity, String>  {
}
