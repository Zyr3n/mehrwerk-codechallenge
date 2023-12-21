package de.jsiemssen.mehrwerkcodechallenge.controller;

import de.jsiemssen.mehrwerkcodechallenge.model.Shop;
import de.jsiemssen.mehrwerkcodechallenge.service.ShopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shops")
public class ShopController {
	private final ShopService shopService;

	public ShopController(ShopService shopService) {
		this.shopService = shopService;
	}

	@GetMapping
	public List<Shop> getAllShops() {
		return shopService.getAllShops();
	}
}