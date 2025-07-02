package com.jangmo.web.repository;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.repository.query.UserCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>,
        UserCustomRepository {

    UserEntity getByRole(UserRole role);

    @Query(value = "SELECT u " +
                     "FROM user u " +
                     "JOIN member m ON u.id = m.id " +
                    "WHERE m.status = :status " +
                      "AND u.role != :role")
    List<UserEntity> findUserByMemberStatusAndRoleNot(
            @Param("status") MemberStatus status,
            @Param("role") UserRole role
    );

    @Query(value = "SELECT u " +
                    "FROM user u " +
                    "JOIN member m ON u.id = m.id " +
                   "WHERE m.status != :status " +
                     "AND u.role != role")
    List<UserEntity> findUserByMemberStatusNotAndRole(
            @Param("status") List<MemberStatus> status,
            @Param("role") UserRole role
    );

    Optional<UserEntity> findByMobile(String mobile);

    @Query(value = "SELECT u FROM user u " +
                "LEFT JOIN member mem ON u.id = mem.id " +
                "LEFT JOIN mercenary mer ON u.id = mer.id " +
                    "WHERE mem.status = 'PENDING' or mer.status = 'PENDING'",
            nativeQuery = true)
    List<UserEntity> findApprovalUsers();

    Page<UserEntity> findByIdNotAndRoleNot(String id, UserRole role, Pageable pageable);
}
