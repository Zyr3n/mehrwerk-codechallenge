package de.jsiemssen.mehrwerkcodechallenge.service;

import de.jsiemssen.mehrwerkcodechallenge.client.ShopAPIClient;
import de.jsiemssen.mehrwerkcodechallenge.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            Shop existingShop = shopRepository.findById(shop.getId())
                    .orElse(null);

            if (existingShop != null) {
                updateExistingShop(existingShop, shop);
            } else {
                for (Category category : shop.getCategories()) {
                    category.addShop(shop);
                }
                shopRepository.save(shop);
            }
        }
    }

    private void updateExistingShop(Shop existingShop, Shop newShop) {
        existingShop.setName(newShop.getName());
        existingShop.setDescription(newShop.getDescription());
        setShopInCategories(newShop);
        existingShop.setCategories(newShop.getCategories());
        shopRepository.save(existingShop);
    }

    private void setShopInCategories(Shop shop) {
        for (Category category : shop.getCategories()) {
            category.setShop(shop);
        }
    }

    @Transactional(readOnly = true)
    public List<ShopDTO> getAllShops() {
        List<Shop> shops = shopRepository.findAll();
        List<ShopDTO> shopDTOs = new ArrayList<>();

        for (Shop shop : shops) {
            shopDTOs.add(convertToShopDTO(shop));
        }

        return shopDTOs;
    }

    private ShopDTO convertToShopDTO(Shop shop) {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setId(shop.getId());
        shopDTO.setName(shop.getName());
        shopDTO.setDescription(shop.getDescription());

        Set<CategoryDTO> categoryDTOs = new HashSet<>();
        for (Category category : shop.getCategories()) {
            categoryDTOs.add(convertToCategoryDTO(category));
        }
        shopDTO.setCategories(categoryDTOs);

        return shopDTO;
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }
}
