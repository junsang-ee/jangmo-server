package com.jangmo.web.repository;

import com.jangmo.web.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    Optional<MemberEntity> findByName(String name);

    Optional<MemberEntity> findByMobile(int mobile);
}
