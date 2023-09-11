package com.example.usersdb.repositories;

import com.example.usersdb.entities.TagsForProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsForProductsRepository extends JpaRepository<TagsForProducts, Long> {
}
