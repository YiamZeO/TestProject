package com.example.usersdb.repositories;

import com.example.usersdb.entities.ProductInOrder;
import com.example.usersdb.entities.ProductInOrderCompKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInOrdersRepository extends JpaRepository<ProductInOrder, ProductInOrderCompKey> {
}
