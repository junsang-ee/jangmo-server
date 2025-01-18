package com.jangmo.web.repository;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.UserStatus;
import com.jangmo.web.model.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    List<UserEntity> findByRoleNotAndStatus(UserRole role, UserStatus status);

    Optional<UserEntity> findByMobile(String mobile);
}
