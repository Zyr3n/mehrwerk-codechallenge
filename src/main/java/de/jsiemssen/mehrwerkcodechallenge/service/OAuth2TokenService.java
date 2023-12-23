package de.jsiemssen.mehrwerkcodechallenge.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class OAuth2TokenService {
	private Oauth2Token cachedToken;

	public Oauth2Token getCachedToken() {
		return cachedToken;
	}

	public void setCachedToken(Oauth2Token cachedToken) {
		this.cachedToken = cachedToken;
	}

	public static class Oauth2Token {
		private String tokenString;
		private Instant fetchDate;

		public String getTokenString() {
			return tokenString;
		}

		public void setTokenString(String tokenString) {
			this.tokenString = tokenString;
		}

		public Instant getFetchDate() {
			return fetchDate;
		}

		public void setFetchDate(Instant fetchDate) {
			this.fetchDate = fetchDate;
		}

		public Oauth2Token(String tokenString, Instant fetchDate){
			this.tokenString = tokenString;
			this.fetchDate = fetchDate;
		}

		public boolean isExpired(){
			return this.fetchDate.isBefore(Instant.now().minus(23, ChronoUnit.HOURS).minus(55, ChronoUnit.MINUTES));
		}
	}
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
		if(this.cachedToken != null && !this.cachedToken.isExpired()){
			return Mono.just(this.cachedToken.getTokenString());
		}

		ObjectNode body = new ObjectMapper().createObjectNode();
		body.put("client_id", "bewerber");
		body.put("client_secret", clientSecret);
		body.put("grant_type", "client_credentials");
		return webClient.post()
				.uri(uri).header("X-API-KEY", apiKey)
				.body(BodyInserters.fromValue(body))
				.retrieve()
				.bodyToMono(JsonNode.class)
				.map(response -> {
					this.cachedToken = new Oauth2Token(response.get("access_token").asText(), Instant.now());
					return this.cachedToken.getTokenString();
				});
	}
}