package com.jangmo.web.repository;

import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MercenaryTransientRepository extends JpaRepository<MercenaryTransientEntity, Long> {

    Optional<MercenaryTransientEntity> findByMercenary(MercenaryEntity mercenary);
}
