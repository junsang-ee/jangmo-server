package com.jangmo.web.service.aspect;


import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.AuthException;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.response.SearchPlaceResponse;
import com.jangmo.web.model.entity.api.KakaoApiUsageEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.KakaoApiUsageRepository;
import com.jangmo.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Aspect
@Component
public class KakaoApiAspect {

    private final MemberRepository memberRepository;

    private final KakaoApiUsageRepository kakaoApiUsageRepository;

    @Transactional
    @Around("execution(* com..GroundManagementService.searchGrounds(..)) && args(searcherId, ..)")
    public List<SearchPlaceResponse> enforceKakaoQuota(ProceedingJoinPoint point, String searcherId)
            throws Throwable {
        MemberEntity apiCaller = memberRepository.findById(searcherId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );
        KakaoApiUsageEntity apiUsage = kakaoApiUsageRepository.findByApiCaller(
                apiCaller
        ).orElseThrow(
                () -> new AuthException(ErrorMessage.AUTH_KAKAO_API_DENIED)
        );
        if (apiUsage.getUsedCount() >= 50)
            throw new AuthException(ErrorMessage.AUTH_KAKAO_API_EXCEEDED);

        @SuppressWarnings("unchecked")
        List<SearchPlaceResponse> result = (List<SearchPlaceResponse>) point.proceed();
        apiUsage.increaseUsedCount();
        return result;
    }

}
