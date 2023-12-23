package de.jsiemssen.mehrwerkcodechallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {
    @Id
    private String id;
    private String name;
    @ManyToMany(mappedBy = "categories")
    private Set<Shop> shops = new HashSet<>();

    public Set<Shop> getShops() {
        return shops;
    }

    public void setShops(Set<Shop> shops) {
        this.shops = shops;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addShop(Shop shop) {
        this.shops.add(shop);
    }

    public void setShop(Shop shop) {
        this.shops.add(shop);
    }
}
