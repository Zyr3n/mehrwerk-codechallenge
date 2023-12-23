package de.jsiemssen.mehrwerkcodechallenge.runner;

import de.jsiemssen.mehrwerkcodechallenge.service.ShopService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ShopDatabaseInitializer implements ApplicationRunner {
	private final ShopService shopService;

	public ShopDatabaseInitializer(ShopService shopService) {
		this.shopService = shopService;
	}

	private void initializeShops() throws IOException {
		this.shopService.initializeShopsTable();
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		initializeShops();
	}
}