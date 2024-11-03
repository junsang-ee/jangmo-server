package com.jangmo.web.security;

import com.jangmo.web.model.entity.MemberEntity;
import com.jangmo.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final Supplier<UserService> serviceSupplier;

    @Override
    public UserDetails loadUserByMobile(int mobile) throws UsernameNotFoundException {
        return serviceSupplier.get().getByMobile(mobile).map(this::convert)
                .orElseThrow(
                        () -> new UsernameNotFoundException(mobile + " 번호로 등록된 사용자 정보를 찾을 수 없습니다.")
                );
    }

    @Override
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        return serviceSupplier.get().get(userId).map(this::convert)
                .orElseThrow(
                        () -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다.")
                );
    }

    private UserDetails convert(MemberEntity entity){
        return new ExtendedUserDetails(
                entity.getId(),
                entity.getMobile(),
                entity.getRole()
        );
    }

}
