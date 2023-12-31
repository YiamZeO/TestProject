package com.example.usersdb.controllers;

import com.example.usersdb.dto.ProductDTO;
import com.example.usersdb.dto.ProductSpecDTO;
import com.example.usersdb.dto.ProductsSpecAndExcelDataStyleDTO;
import com.example.usersdb.entities.Product;
import com.example.usersdb.responseObjects.FilteringResponseObj;
import com.example.usersdb.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/products")
@Validated
public class ProductsController {
    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {
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
    public FilteringResponseObj productsWithFiltering(@RequestBody ProductSpecDTO productSpecDTO) {
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

    @GetMapping("/products_list_excel")
    public ResponseEntity<byte[]> productsWithFilteringExcel(@RequestBody @Valid ProductsSpecAndExcelDataStyleDTO productsSpecAndExcelDataStyleDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "Products_table.xlsx");
        return ResponseEntity.ok().headers(headers).
                body(productsService.getProductsWithPgAndFlExcel(productsSpecAndExcelDataStyleDTO.getProductSpecDTO(),
                        productsSpecAndExcelDataStyleDTO.getExcelDataStyleDTO()));
    }

    @GetMapping("/products_list_stream_case")
    public FilteringResponseObj productsWithFilteringStreamCase(@RequestBody ProductSpecDTO productSpecDTO) {
        return productsService.getProductsWithPgAndFl(productSpecDTO);
    }

    @DeleteMapping("/del_product_tag_jdbc")
    public List<Product> deleteProductTagJdbc(@RequestParam @Min(0) Long productId,
                                              @RequestParam @Min(0) Long tagId) {
        return productsService.delProductTagJdbc(productId, tagId);
    }

    @PostMapping("/add_product_tag_jdbc")
    public List<Product> addProductTagJdbc(@RequestParam @Min(0) Long productId,
                                           @RequestParam @Min(0) Long tagId) {
        return productsService.addProductTagJdbc(productId, tagId);
    }

    @DeleteMapping("/del_product_jdbc")
    @Secured(value = "ADMIN")
    public List<Product> deleteProductJdbc(@RequestParam @Min(0) Long id) {
        return productsService.deleteProductJdbc(id);
    }

    @PostMapping("/add_product_jdbc")
    @Secured(value = "ADMIN")
    public List<Product> addProductJdbc(@RequestBody @Valid ProductDTO productDTO) {
        return productsService.addProductJdbc(productDTO);
    }

    @GetMapping("/products_list_jdbc")
    public FilteringResponseObj productsWithFilteringJdbc(@RequestBody ProductSpecDTO productSpecDTO) {
        return productsService.getProductsWithPgAndFlJdbc(productSpecDTO);
    }

}
