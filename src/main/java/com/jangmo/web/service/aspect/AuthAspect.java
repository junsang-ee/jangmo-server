package com.jangmo.web.service.aspect;

import com.jangmo.web.constants.AuthPurposeType;
import com.jangmo.web.constants.cache.CacheType;
import com.jangmo.web.infra.cache.CacheAccessor;
import com.jangmo.web.model.dto.request.VerificationCodeSendRequest;
import com.jangmo.web.model.dto.request.VerificationCodeVerifyRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
public class AuthAspect {

    private final static String SEND_CODE_METHOD_NAME = "sendAuthCode";
    private final static String VERIFY_CODE_METHOD_NAME = "verifyCode";

    private final CacheAccessor cacheAccessor;

    @AfterReturning(
            value = "execution(* com..AuthService.sendAuthCode(..)) || execution(* com..AuthService.verifyCode(..))",
            returning = "authCode")
    public void handleCachedAuthCode(JoinPoint joinPoint, Object authCode) {
        Object request = joinPoint.getArgs()[0];
        String method = joinPoint.getSignature().getName();
        CacheType cacheType = getCacheType(request);
        String mobile = getMobile(request);

        cacheAccessor.get(cacheType, mobile, String.class).ifPresent(
                cachedAuthCode -> {
                    if (method.equals(VERIFY_CODE_METHOD_NAME)) {
                        String inputCode = ((VerificationCodeVerifyRequest) request).getCode();
                        if (!inputCode.equals(cachedAuthCode))
                            return;
                    }
                    cacheAccessor.remove(cacheType, mobile);
                }
        );
        if (method.equals(SEND_CODE_METHOD_NAME)) {
            if (authCode instanceof String) {
                cacheAccessor.put(cacheType, mobile, (String)authCode);
            }
        }

    }

    private CacheType getCacheType(Object request) {
        AuthPurposeType authPurposeType = request instanceof VerificationCodeSendRequest ?
            ((VerificationCodeSendRequest) request).getAuthPurposeType() :
            ((VerificationCodeVerifyRequest) request).getAuthPurposeType();
        return authPurposeType == AuthPurposeType.SIGNUP ?
                CacheType.SIGNUP_CODE : CacheType.RESET_CODE;
    }

    private String getMobile(Object request) {
        return request instanceof VerificationCodeSendRequest ?
            ((VerificationCodeSendRequest) request).getMobile() :
            ((VerificationCodeVerifyRequest) request).getMobile();
    }
}
