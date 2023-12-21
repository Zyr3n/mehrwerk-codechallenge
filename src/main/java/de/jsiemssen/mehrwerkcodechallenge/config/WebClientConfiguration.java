package de.jsiemssen.mehrwerkcodechallenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

	@Value("${application.keys.api}")
	private String apiKey;

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
				.defaultHeader("X-API-KEY", apiKey)
				.build();
	}
}