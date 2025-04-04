package com.jangmo.web.repository.query;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.model.dto.request.UserListSearchRequest;
import com.jangmo.web.model.dto.response.UserListResponse;
import com.jangmo.web.model.vo.UserStatusVO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.jangmo.web.model.entity.user.QUserEntity.userEntity;
import static com.jangmo.web.model.entity.user.QMemberEntity.memberEntity;
import static com.jangmo.web.model.entity.user.QMercenaryEntity.mercenaryEntity;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserListResponse> findByStatusAndUserRole(
            String adminId,
            String myId,
            UserListSearchRequest request,
            Pageable pageable) {
        List<UserListResponse> result = queryFactory
                .select(Projections.constructor(
                        UserListResponse.class,
                        userEntity.id,
                        userEntity.name,
                        userEntity.role,
                        Projections.constructor(
                                UserStatusVO.class,
                                memberEntity.status,
                                mercenaryEntity.status
                        )
                ))
                .from(userEntity)
                .leftJoin(memberEntity).on(userEntity.id.eq(memberEntity.id))
                .leftJoin(mercenaryEntity).on(userEntity.id.eq(mercenaryEntity.id))
                .where(
                        userEntity.id.ne(myId),
                        userEntity.id.ne(adminId),
                        userRoleEq(request.getRole()),
                        statusEq(
                                request.getRole(),
                                request.getMemberStatus(),
                                request.getMercenaryStatus()
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(memberEntity.count())
                .from(userEntity)
                .leftJoin(memberEntity).on(userEntity.id.eq(memberEntity.id))
                .leftJoin(mercenaryEntity).on(userEntity.id.eq(mercenaryEntity.id))
                .where(
                        userEntity.id.ne(myId),
                        userEntity.id.ne(adminId),
                        userRoleEq(request.getRole()),
                        statusEq(
                            request.getRole(),
                            request.getMemberStatus(),
                            request.getMercenaryStatus()
                        )
                );
        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);
    }

    private BooleanExpression userRoleEq(final UserRole role) {
        return !ObjectUtils.isEmpty(role) ? userEntity.role.eq(role) : null;
    }

    private BooleanExpression statusEq(
            final UserRole role,
            final MemberStatus memberStatus,
            final MercenaryStatus mercenaryStatus) {
        if (!ObjectUtils.isEmpty(role)) {
            if (role == UserRole.MEMBER) {
                return memberStatus != null ? memberEntity.status.eq(memberStatus) : null;
            } else if (role == UserRole.MERCENARY)
                return mercenaryStatus != null ? mercenaryEntity.status.eq(mercenaryStatus) : null;
        }
        BooleanExpression memberCondition =
                memberStatus != null ? memberEntity.status.eq(memberStatus) : null;
        BooleanExpression mercenaryCondition =
                mercenaryStatus != null ? mercenaryEntity.status.eq(mercenaryStatus) : null;
        return combineUserStatusCondition(memberCondition, mercenaryCondition);
    }

    private BooleanExpression combineUserStatusCondition(BooleanExpression... conditions) {
        BooleanExpression result = null;
        for (BooleanExpression condition : conditions) {
            if (condition != null)
                result = result == null ? condition : result.or(condition);
        }
        return result;
    }

}
