package de.jsiemssen.mehrwerkcodechallenge.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jsiemssen.mehrwerkcodechallenge.model.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;

@Service
public class ShopAPIClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShopAPIClient.class);
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	@Value("${application.uris.shops}")
	private String uri;

	public ShopAPIClient(WebClient webClient, ObjectMapper objectMapper) {
		this.webClient = webClient;
		this.objectMapper = objectMapper;
	}

	public List<Shop> getShops() throws IOException {

		ResponseEntity<JsonNode> responseEntity = webClient.get().uri(uri).retrieve().toEntity(JsonNode.class).block();

		List<Shop> responseBody;

		if (responseEntity != null && responseEntity.getBody() != null && responseEntity.getBody().get("items") != null) {
			responseBody = objectMapper.readerFor(new TypeReference<List<Shop>>() {
			}).readValue(responseEntity.getBody().get("items"));

		} else {
			throw new ServerException("Error while loading Shops: No data was fetched");
		}

		return responseBody;
	}
}