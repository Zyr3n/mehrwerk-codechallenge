package de.jsiemssen.mehrwerkcodechallenge.service;

import de.jsiemssen.mehrwerkcodechallenge.client.ShopAPIClient;
import de.jsiemssen.mehrwerkcodechallenge.model.Shop;
import de.jsiemssen.mehrwerkcodechallenge.model.ShopRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ShopService {
	private final ShopAPIClient shopAPIClient;
	private final ShopRepository shopRepository;

	public ShopService(ShopAPIClient shopAPIClient, ShopRepository shopRepository) {
		this.shopAPIClient = shopAPIClient;
		this.shopRepository = shopRepository;
	}


	public void initializeShopsTable() throws IOException {
		List<Shop> shops = shopAPIClient.getShops();
		for (Shop shop : shops) {
			shopRepository.findById(shop.getId())
					.ifPresentOrElse(
							existingShop -> updateExistingShop(existingShop, shop),
							() -> shopRepository.save(shop)
					);
		}
	}

	private void updateExistingShop(Shop existingShop, Shop newShop) {
		existingShop.setName(newShop.getName());
		existingShop.setDescription(newShop.getDescription());
		existingShop.setCategories(newShop.getCategories());
		shopRepository.save(existingShop);
	}

	public List<Shop> getAllShops() {
		return shopRepository.findAll();
	}
}
