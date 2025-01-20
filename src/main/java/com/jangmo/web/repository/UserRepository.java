package com.jangmo.web.repository;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.MemberStatus;
import com.jangmo.web.model.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query("SELECT u " +
             "FROM user u " +
             "JOIN member m ON u.id = m.id " +
            "WHERE m.status = :status " +
              "AND u.role != :role")
    List<UserEntity> findUserByMemberStatusAndRoleNot(
            @Param("status") MemberStatus status,
            @Param("role") UserRole role
    );

    Optional<UserEntity> findByMobile(String mobile);
}
