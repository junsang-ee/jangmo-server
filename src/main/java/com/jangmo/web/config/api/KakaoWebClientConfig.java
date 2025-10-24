package com.jangmo.web.config.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jangmo.web.config.properties.KakaoApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@DependsOn("webClientConfiguration")
@Configuration
public class KakaoWebClientConfig {

    private final KakaoApiProperties kakaoProperties;

    @Bean(name = "kakaoWebClient")
    public WebClient kakaoWebClient(WebClient.Builder webClientBuilder,
                                    ObjectMapper objectMapper,
                                    ExchangeStrategies defaultExchangeStrategies) {
        ExchangeStrategies strategies = defaultExchangeStrategies.mutate()
                .codecs(config -> {
                    config.defaultCodecs().jackson2JsonEncoder(
                            new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON)
                    );
                    config.defaultCodecs().jackson2JsonDecoder(
                            new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON)
                    );
                })
                .build();

        return webClientBuilder
                .baseUrl(kakaoProperties.uri())
                .exchangeStrategies(strategies)
                .defaultHeader("Authorization", "KakaoAK " + kakaoProperties.key())
                .build();
    }

}
