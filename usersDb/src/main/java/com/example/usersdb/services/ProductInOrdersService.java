package com.example.usersdb.services;

import com.example.usersdb.repositories.ProductInOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInOrdersService {
    private final ProductInOrdersRepository productInOrdersRepository;

    @Autowired
    public ProductInOrdersService(ProductInOrdersRepository productInOrdersRepository) {
        this.productInOrdersRepository = productInOrdersRepository;
    }
}
