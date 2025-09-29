package com.jangmo.web.repository.user;

import com.jangmo.web.model.entity.user.MercenaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MercenaryRepository extends JpaRepository<MercenaryEntity, String> {
    Optional<MercenaryEntity> findByMobile(String mobile);
}
