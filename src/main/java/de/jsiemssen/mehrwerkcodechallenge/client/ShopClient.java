package de.jsiemssen.mehrwerkcodechallenge.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ShopClient implements ApplicationRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShopClient.class);
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	@Value("${application.uris.shops}")
	private String uri;

	public ShopClient(WebClient webClient, ObjectMapper objectMapper) {
		this.webClient = webClient;
		this.objectMapper = objectMapper;
	}

	public JsonNode getShops() {

		ResponseEntity<JsonNode> responseEntity = webClient.get().uri(uri).retrieve().toEntity(JsonNode.class).block();
		JsonNode responseBody = responseEntity != null ? responseEntity.getBody() : objectMapper.createObjectNode();

		LOGGER.info(responseBody.toString());

		return responseBody;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		getShops();
	}
}