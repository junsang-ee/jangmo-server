package com.jangmo.web.security;

import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class ExtendedUserDetailServiceImpl implements ExtendedUserDetailsService {

    private final Supplier<UserService> serviceSupplier;

    @Override
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        return serviceSupplier.get().findMemberByMobile(mobile).map(this::convertToMember)
                .or(
                    () -> serviceSupplier.get().findMercenaryByMobile(mobile).map(this::convertToMercenary)
                ).orElseThrow(
                        () -> new UsernameNotFoundException(mobile + " 번호로 등록된 사용자 정보를 찾을 수 없습니다.")
                );
    }

    @Override
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        return serviceSupplier.get().findMemberById(userId).map(this::convertToMember)
                .or(
                        () -> serviceSupplier.get().findMercenaryById(userId).map(this::convertToMercenary)
                )
                .orElseThrow(
                        () -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다.")
                );
    }

    @Deprecated
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("This method is not supported for mobile-based authentication.");
    }

    private UserDetails convertToMember(MemberEntity member){
        return new ExtendedUserDetails(
                member.getId(),
                member.getMobile(),
                member.getRole()
        );
    }

    private UserDetails convertToMercenary(MercenaryEntity mercenary) {
        return new ExtendedUserDetails(
                mercenary.getId(),
                mercenary.getMobile(),
                mercenary.getRole()
        );
    }

}
