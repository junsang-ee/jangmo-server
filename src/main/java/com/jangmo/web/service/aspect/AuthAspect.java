package com.jangmo.web.service.aspect;

import com.jangmo.web.constants.cache.CacheType;
import com.jangmo.web.model.dto.request.MobileRequest;
import com.jangmo.web.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
public class AuthAspect {

    private final CacheService cacheService;

    @AfterReturning(value = "execution(* com..AuthService.sendAuthCode(..)) && args(request)",
            returning = "code", argNames = "request, code")
    public void sendCode(MobileRequest request, String code) {
        try {
            if (cacheService.get(CacheType.SIGNUP_CODE, request.getMobile()) != null) {
                cacheService.remove(
                        CacheType.SIGNUP_CODE,
                        request.getMobile()
                );
            }
        } catch (NullPointerException ignored) {
        } finally {
            cacheService.put(
                    CacheType.SIGNUP_CODE,
                    request.getMobile(),
                    code
            );
        }
    }
}
