package de.jsiemssen.mehrwerkcodechallenge.config;

import de.jsiemssen.mehrwerkcodechallenge.service.OAuth2TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

	@Value("${application.keys.api}")
	private String apiKey;

	@Bean
	public WebClient webClient(OAuth2TokenService tokenService) {
		return WebClient.builder()
				.defaultHeader("X-API-KEY", apiKey)
				.filter((request, next) -> tokenService.fetchToken()
						.flatMap(token -> {
							ClientRequest modifiedRequest = ClientRequest.from(request)
									.header("Authorization", "Bearer " + token)
									.build();
							return next.exchange(modifiedRequest);
						}))
				.build();
	}
}