package com.jangmo.web.repository;

import com.jangmo.web.model.entity.api.KakaoApiUsageEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoApiUsageRepository extends JpaRepository<KakaoApiUsageEntity, String> {
	Optional<KakaoApiUsageEntity> findByApiCaller(MemberEntity apiCaller);

	void deleteByApiCaller(MemberEntity apiCaller);
}