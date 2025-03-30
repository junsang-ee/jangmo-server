package com.jangmo.web.repository;

import com.jangmo.web.model.entity.user.MercenaryTransientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercenaryTransientRepository extends JpaRepository<MercenaryTransientEntity, Long> {
}
