package de.jsiemssen.mehrwerkcodechallenge.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, String> {
}