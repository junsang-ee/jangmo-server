package com.jangmo.web.repository.criteria;

import com.jangmo.web.config.dsl.Criteria;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.entity.user.QUserEntity;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Setter
@Accessors(chain = true, fluent = true)
public class UserListCriteria extends Criteria {
    private static final QUserEntity USER = QUserEntity.userEntity;

    private String myId;
    private UserRole role;

    @Override
    protected void build(PredicateBuilder predicates) {
        if (StringUtils.isNotEmpty(myId)) {
            predicates.and(ne(USER.id, myId));
        }

        if (ObjectUtils.isNotEmpty(role)) {
            predicates.and(eq(USER.role, role));
        }
    }
}