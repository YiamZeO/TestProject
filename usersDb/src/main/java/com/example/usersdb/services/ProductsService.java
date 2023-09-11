package com.example.usersdb.services;

import com.example.usersdb.dto.ProductDTO;
import com.example.usersdb.dto.ProductSpecDTO;
import com.example.usersdb.entities.*;
import com.example.usersdb.repositories.ProductsRepository;
import com.example.usersdb.repositories.TagsForProductsRepository;
import com.example.usersdb.repositories.specs.ProductsSpecs;
import com.example.usersdb.responsObjects.FilteringResponsObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {
    private static final int INITIAL_PAGE = 1;
    private static final int PAGE_SIZE = 3;
    private final ProductsRepository productsRepository;
    private final TagsForProductsRepository tagsForProductsRepository;
    @Autowired
    public ProductsService(ProductsRepository productsRepository, TagsForProductsRepository tagsForProductsRepository) {
        this.productsRepository = productsRepository;
        this.tagsForProductsRepository = tagsForProductsRepository;
    }
    public FilteringResponsObject getProductsWithPgAndFl(ProductSpecDTO productSpecDTO){
        Specification<Product> spec = Specification.where(null);
        FilteringResponsObject res = new FilteringResponsObject();
        if (productSpecDTO.getCurPage() == null || productSpecDTO.getCurPage() < 1)
            productSpecDTO.setCurPage(INITIAL_PAGE);
        if(productSpecDTO.getPageSize() == null || productSpecDTO.getPageSize() < 1)
            productSpecDTO.setPageSize(PAGE_SIZE);
        if (productSpecDTO.getName() != null && !productSpecDTO.getName().isEmpty())
            spec = spec.and(ProductsSpecs.nameIs(productSpecDTO.getName()));
        if (productSpecDTO.getMinD() != null)
            spec = spec.and(ProductsSpecs.dateGrThenOrEq(productSpecDTO.getMinD()));
        if (productSpecDTO.getMaxD() != null)
            spec = spec.and(ProductsSpecs.dateLeThenOrEq(productSpecDTO.getMaxD()));
        if (productSpecDTO.getDescrContain() != null && !productSpecDTO.getDescrContain().isEmpty())
            spec = spec.and(ProductsSpecs.descriptionContain(productSpecDTO.getDescrContain()));
        if (productSpecDTO.getMinCost() != null)
            spec = spec.and(ProductsSpecs.costGrThenOrEq(productSpecDTO.getMinCost()));
        if (productSpecDTO.getMaxCost() != null)
            spec = spec.and(ProductsSpecs.costLeThenOrEq(productSpecDTO.getMaxCost()));
        if (productSpecDTO.getMinQuality() != null)
            spec = spec.and(ProductsSpecs.qualityGrThenOrEq(productSpecDTO.getMinQuality()));
        if (productSpecDTO.getMaxQuality() != null)
            spec = spec.and(ProductsSpecs.qualityLeThenOrEq(productSpecDTO.getMaxQuality()));
        if (productSpecDTO.getTagsIdList() != null && !productSpecDTO.getTagsIdList().isEmpty())
            for(Long id : productSpecDTO.getTagsIdList()){
                Optional<TagsForProducts> tag = tagsForProductsRepository.findById(id);
                if(tag.isPresent())
                    spec = spec.or(ProductsSpecs.isContainTag(tag.get()));
            }
        Page<Product> products = productsRepository.findAll(spec, PageRequest.of(productSpecDTO.getCurPage() - 1,
                productSpecDTO.getPageSize()));
        res.setCurrentPage(productSpecDTO.getCurPage());
        res.setTotalPages(products.getTotalPages());
        res.setPageSize(productSpecDTO.getPageSize());
        res.setDataList(products.getContent());
        return res;
    }
    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> addProduct(ProductDTO productDTO){
        productsRepository.save(new Product(productDTO));
        return productsRepository.findAll();
    }
    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> deleteById(Long id){
        productsRepository.deleteById(id);
        return productsRepository.findAll();
    }
    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> updateProduct(Long id, ProductDTO productDTO){
        Optional<Product> o = productsRepository.findById(id);
        if (o.isPresent()){
            Product p = o.get();
            p.setName(productDTO.getName());
            p.setCost(productDTO.getCost());
            p.setDescription(p.getDescription());
            p.setDate(productDTO.getDate());
            p.setQuality(p.getQuality());
            productsRepository.save(p);
        }
        return productsRepository.findAll();
    }
    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> addProductTag(Long productId, Long tagId){
        Optional<Product> p = productsRepository.findById(productId);
        Optional<TagsForProducts> t = tagsForProductsRepository.findById(tagId);
        if (p.isEmpty() || t.isEmpty())
            return (List<Product>) null;
        p.get().addTag(t.get());
        return productsRepository.findAll();
    }
    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> delProductTag(Long productId, Long tagId){
        Optional<Product> p = productsRepository.findById(productId);
        Optional<TagsForProducts> t = tagsForProductsRepository.findById(tagId);
        if (p.isEmpty() || t.isEmpty())
            return (List<Product>) null;
        p.get().delTag(t.get());
        return productsRepository.findAll();
    }
}
