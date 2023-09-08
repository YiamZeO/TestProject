package com.example.usersdb.controllers;

import com.example.usersdb.DTOs.ProductDTO;
import com.example.usersdb.DTOs.ProductSpecDTO;
import com.example.usersdb.entities.Product;
import com.example.usersdb.services.ProductsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {
    private final ProductsService productsService;

    @Autowired
    public ProductController(ProductsService productsService) {
        this.productsService = productsService;
    }
    @PostMapping("/addProduct")
    @Secured(value = "ADMIN")
    public List<Product> addProduct(@RequestBody @Valid ProductDTO productDTO){
        return productsService.addProduct(productDTO);
    }
    @DeleteMapping("/delProduct")
    @Secured(value = "ADMIN")
    public List<Product> deleteProduct(@RequestParam @Min(0) Long id){
        return productsService.deleteById(id);
    }
    @PostMapping("/uppProduct")
    @Secured(value = "ADMIN")
    public List<Product> updateProduct(@RequestParam @Min(0) Long id, @RequestBody @Valid ProductDTO productDTO){
        return productsService.updateProduct(id, productDTO);
    }
    @GetMapping("getProducts")
    public List<Product> getProducts(@RequestBody ProductSpecDTO productSpecDTO){
        return productsService.getProductsWithPgAndFl(productSpecDTO);
    }
}
