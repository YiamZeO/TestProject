package com.example.usersdb.controllers;

import com.example.usersdb.dto.ProductDTO;
import com.example.usersdb.dto.ProductSpecDTO;
import com.example.usersdb.entities.Product;
import com.example.usersdb.responsObjects.FilteringResponsObject;
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

    @PostMapping("/add_product")
    @Secured(value = "ADMIN")
    public List<Product> addProduct(@RequestBody @Valid ProductDTO productDTO) {
        return productsService.addProduct(productDTO);
    }

    @DeleteMapping("/del_product")
    @Secured(value = "ADMIN")
    public List<Product> deleteProduct(@RequestParam @Min(0) Long id) {
        return productsService.deleteById(id);
    }

    @PostMapping("/upp_product")
    @Secured(value = "ADMIN")
    public List<Product> updateProduct(@RequestParam @Min(0) Long id, @RequestBody @Valid ProductDTO productDTO) {
        return productsService.updateProduct(id, productDTO);
    }

    @GetMapping("/products_list")
    public FilteringResponsObject productsWithFiltering(@RequestBody ProductSpecDTO productSpecDTO) {
        return productsService.getProductsWithPgAndFl(productSpecDTO);
    }

    @GetMapping("/products_list_stream_case")
    public FilteringResponsObject productsWithFilteringStreamCase(@RequestBody ProductSpecDTO productSpecDTO) {
        return productsService.getProductsWithPgAndFl(productSpecDTO);
    }

    @DeleteMapping("/del_product_tag")
    public List<Product> deleteProductTag(@RequestParam @Min(0) Long productId,
                                          @RequestParam @Min(0) Long tagId) {
        return productsService.delProductTag(productId, tagId);
    }

    @PostMapping("/add_product_tag")
    public List<Product> addProductTag(@RequestParam @Min(0) Long productId,
                                       @RequestParam @Min(0) Long tagId) {
        return productsService.addProductTag(productId, tagId);
    }

    @GetMapping("/products_find_all_jdbc")
    public List<Product> productsFindAllJdbc() {
        return productsService.findAllJdbc();
    }
}
