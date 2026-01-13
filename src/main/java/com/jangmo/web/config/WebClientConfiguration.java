package com.jangmo.web.config;

import io.netty.channel.ChannelOption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {

	HttpClient httpClient = HttpClient.create()
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

	@Bean
	public ExchangeStrategies defaultExchangeStrategies() {
		return ExchangeStrategies.builder().codecs(
			config -> {
				config.defaultCodecs().maxInMemorySize(1024 * 1024);
			}
		).build();
	}

    @Bean
    public WebClient.Builder webClientBuilder() {
			return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
