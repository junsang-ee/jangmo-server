package com.jangmo.web.api;

import com.jangmo.web.constants.ApiType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.InvalidStateException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class WebClientApiHelper {
    private final static String KAKAO_PATH = "keyword.json?query=";

    public <T> Mono<T> get(WebClient webClient, ApiType type, String query, Class<T> response) {
        String queryPath = "";
        if (type == ApiType.KAKAO) {
            queryPath = KAKAO_PATH + query ;
        }
        return webClient.get()
                .uri(queryPath)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> Mono.error(
                                new InvalidStateException(ErrorMessage.BAD_REQUEST)
                        )
                )
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.error(
                                new InvalidStateException(ErrorMessage.INTERNAL_SERVER_ERROR)
                        )
                )
                .bodyToMono(response)
                .doOnError(e -> log.error("Error occurred during WebClient call", e));
    }

}
