package com.jangmo.web.service;

import com.jangmo.web.api.WebClientApiHelper;
import com.jangmo.web.constants.ApiType;
import com.jangmo.web.model.dto.response.KakaoReseponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroundServiceImpl implements GroundService {

    private final WebClientApiHelper webClientApiHelper;

    @Qualifier("kakaoWebClient")
    private final WebClient webClient;

    public void test(String keyword) {
        log.info("test : {}",
                webClientApiHelper.get(
                        webClient, ApiType.KAKAO, keyword, KakaoReseponse.class
                ));
    }
}
