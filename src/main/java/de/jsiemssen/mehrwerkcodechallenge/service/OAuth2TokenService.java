package de.jsiemssen.mehrwerkcodechallenge.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OAuth2TokenService {
	private final WebClient webClient;
	@Value("${application.uris.token}")
	private String uri;
	@Value("${application.keys.client-secret}")
	private String clientSecret;
	@Value("${application.keys.api}")
	private String apiKey;

	public OAuth2TokenService() {
		this.webClient = WebClient.builder()
				.build();
	}

	public OAuth2TokenService(WebClient webClient) {
		this.webClient = webClient;
	}

	public Mono<String> fetchToken() {
		ObjectNode body = new ObjectMapper().createObjectNode();
		body.put("client_id", "bewerber");
		body.put("client_secret", clientSecret);
		body.put("grant_type", "client_credentials");
		return webClient.post()
				.uri(uri).header("X-API-KEY", apiKey)
				.body(BodyInserters.fromValue(body))
				.retrieve()
				.bodyToMono(JsonNode.class)
				.map(response -> response.get("access_token").asText());
	}
}