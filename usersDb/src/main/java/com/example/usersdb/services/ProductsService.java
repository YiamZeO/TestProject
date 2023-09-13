package com.example.usersdb.services;

import com.example.usersdb.dto.ProductDTO;
import com.example.usersdb.dto.ProductSpecDTO;
import com.example.usersdb.entities.Product;
import com.example.usersdb.entities.TagsForProducts;
import com.example.usersdb.repositories.ProductsRepository;
import com.example.usersdb.repositories.ProductsRepositoryJdbc;
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
import java.util.stream.Stream;

@Service
public class ProductsService {
    private static final int INITIAL_PAGE = 1;
    private static final int PAGE_SIZE = 3;
    private final ProductsRepository productsRepository;
    private final TagsForProductsRepository tagsForProductsRepository;
    private final ProductsRepositoryJdbc productsRepositoryJdbc;

    @Autowired
    public ProductsService(ProductsRepository productsRepository, TagsForProductsRepository tagsForProductsRepository, ProductsRepositoryJdbc productsRepositoryJdbc) {
        this.productsRepository = productsRepository;
        this.tagsForProductsRepository = tagsForProductsRepository;
        this.productsRepositoryJdbc = productsRepositoryJdbc;
    }

    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> addProductTagJdbc(Long productId, Long tagId) {
        Product p = productsRepositoryJdbc.findById(productId);
        Optional<TagsForProducts> t = tagsForProductsRepository.findById(tagId);
        if (p != null || t.isEmpty())
            return null;
        productsRepositoryJdbc.productAddTag(productId, tagId);
        return productsRepositoryJdbc.findBySpec(new ProductSpecDTO());
    }

    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> delProductTagJdbc(Long productId, Long tagId) {
        Product p = productsRepositoryJdbc.findById(productId);
        Optional<TagsForProducts> t = tagsForProductsRepository.findById(tagId);
        if (p != null || t.isEmpty())
            return null;
        productsRepositoryJdbc.productDelTag(productId, tagId);
        return productsRepositoryJdbc.findBySpec(new ProductSpecDTO());
    }

    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> deleteProductJdbc(Long id) {
        productsRepositoryJdbc.deleteById(id);
        return productsRepositoryJdbc.findBySpec(new ProductSpecDTO());
    }

    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> updateProductJdbc(Long id, ProductDTO productDTO) {
        Product p = productsRepositoryJdbc.findById(id);
        if (p != null) {
            p.setName(productDTO.getName());
            p.setCost(productDTO.getCost());
            p.setDescription(p.getDescription());
            p.setDate(productDTO.getDate());
            p.setQuality(p.getQuality());
            productsRepositoryJdbc.updateProduct(p, id);
        }
        return productsRepositoryJdbc.findBySpec(new ProductSpecDTO());
    }

    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> addProductJdbc(ProductDTO productDTO) {
        productsRepositoryJdbc.insertProduct(new Product(productDTO));
        return productsRepositoryJdbc.findBySpec(new ProductSpecDTO());
    }

    public FilteringResponsObject getProductsWithPgAndFlJdbc(ProductSpecDTO productSpecDTO) {
        FilteringResponsObject res = new FilteringResponsObject();
        if (productSpecDTO.getCurPage() == null || productSpecDTO.getCurPage() < 1)
            productSpecDTO.setCurPage(INITIAL_PAGE);
        if (productSpecDTO.getPageSize() == null || productSpecDTO.getPageSize() < 1)
            productSpecDTO.setPageSize(PAGE_SIZE);
        List<Product> products = productsRepositoryJdbc.findBySpec(productSpecDTO);
        int totalPages = products.size() / productSpecDTO.getPageSize();
        res.setTotalPages(totalPages == 0 ? 1 : totalPages);
        products = products.stream().limit(((long) productSpecDTO.getPageSize() * productSpecDTO.getCurPage()))
                .skip((long) productSpecDTO.getPageSize() * (productSpecDTO.getCurPage() - 1)).toList();
        res.setCurrentPage(productSpecDTO.getCurPage());
        res.setPageSize(productSpecDTO.getPageSize());
        res.setDataList(products);
        return res;
    }

    public FilteringResponsObject getProductsWithPgAndFl(ProductSpecDTO productSpecDTO) {
        Specification<Product> spec = Specification.where(null);
        FilteringResponsObject res = new FilteringResponsObject();
        if (productSpecDTO.getCurPage() == null || productSpecDTO.getCurPage() < 1)
            productSpecDTO.setCurPage(INITIAL_PAGE);
        if (productSpecDTO.getPageSize() == null || productSpecDTO.getPageSize() < 1)
            productSpecDTO.setPageSize(PAGE_SIZE);
        if (productSpecDTO.getName() != null && !productSpecDTO.getName().isEmpty())
            spec = spec.and(ProductsSpecs.nameIs(productSpecDTO.getName()));
        if (productSpecDTO.getMinDate() != null)
            spec = spec.and(ProductsSpecs.dateGrThenOrEq(productSpecDTO.getMinDate()));
        if (productSpecDTO.getMaxDate() != null)
            spec = spec.and(ProductsSpecs.dateLeThenOrEq(productSpecDTO.getMaxDate()));
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
        if (productSpecDTO.getTagsIdList() != null && !productSpecDTO.getTagsIdList().isEmpty()) {
            for (TagsForProducts tag : tagsForProductsRepository.findAllById(productSpecDTO.getTagsIdList())) {
                spec = spec.or(ProductsSpecs.isContainTag(tag));
            }
        }
        Page<Product> products = productsRepository.findAll(spec, PageRequest.of(productSpecDTO.getCurPage() - 1,
                productSpecDTO.getPageSize()));
        res.setCurrentPage(productSpecDTO.getCurPage());
        res.setTotalPages(products.getTotalPages());
        res.setPageSize(productSpecDTO.getPageSize());
        res.setDataList(products.getContent());
        return res;
    }

    public FilteringResponsObject getProductsWithPgAndFlStreamCase(ProductSpecDTO productSpecDTO) {
        Stream<Product> streamProducts = productsRepository.findAll().stream();
        FilteringResponsObject res = new FilteringResponsObject();
        if (productSpecDTO.getCurPage() == null || productSpecDTO.getCurPage() < 1)
            productSpecDTO.setCurPage(INITIAL_PAGE);
        if (productSpecDTO.getPageSize() == null || productSpecDTO.getPageSize() < 1)
            productSpecDTO.setPageSize(PAGE_SIZE);
        if (productSpecDTO.getName() != null && !productSpecDTO.getName().isEmpty())
            streamProducts = streamProducts.filter(p -> p.getName().equals(productSpecDTO.getName()));
        if (productSpecDTO.getMinDate() != null)
            streamProducts = streamProducts.filter(p -> p.getDate().after(productSpecDTO.getMinDate())
                    || p.getDate().equals(productSpecDTO.getMinDate()));
        if (productSpecDTO.getMaxDate() != null)
            streamProducts = streamProducts.filter(p -> p.getDate().before(productSpecDTO.getMaxDate())
                    || p.getDate().equals(productSpecDTO.getMaxDate()));
        if (productSpecDTO.getDescrContain() != null && !productSpecDTO.getDescrContain().isEmpty())
            streamProducts = streamProducts.filter(p -> p.getDescription().contains(productSpecDTO.getDescrContain()));
        if (productSpecDTO.getMinCost() != null)
            streamProducts = streamProducts.filter(p -> p.getCost() >= productSpecDTO.getMinCost());
        if (productSpecDTO.getMaxCost() != null)
            streamProducts = streamProducts.filter(p -> p.getCost() <= productSpecDTO.getMaxCost());
        if (productSpecDTO.getMinQuality() != null)
            streamProducts = streamProducts.filter(p -> p.getQuality() >= productSpecDTO.getMinQuality());
        if (productSpecDTO.getMaxQuality() != null)
            streamProducts = streamProducts.filter(p -> p.getQuality() <= productSpecDTO.getMaxQuality());
        if (productSpecDTO.getTagsIdList() != null && !productSpecDTO.getTagsIdList().isEmpty()) {
            List<TagsForProducts> tags = tagsForProductsRepository.findAllById(productSpecDTO.getTagsIdList());
            streamProducts = streamProducts.filter(p -> p.getTags().containsAll(tags));
        }
        streamProducts = streamProducts.limit(((long) productSpecDTO.getPageSize() * productSpecDTO.getCurPage()))
                .skip((long) productSpecDTO.getPageSize() * (productSpecDTO.getCurPage() - 1));
        List<Product> resListProducts = streamProducts.toList();
        res.setCurrentPage(productSpecDTO.getCurPage());
        int totalPages = resListProducts.size() / productSpecDTO.getPageSize();
        res.setTotalPages(totalPages == 0 ? 1 : totalPages);
        res.setPageSize(productSpecDTO.getPageSize());
        res.setDataList(resListProducts);
        return res;
    }

    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> addProduct(ProductDTO productDTO) {
        productsRepository.save(new Product(productDTO));
        return productsRepository.findAll();
    }

    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> deleteById(Long id) {
        productsRepository.deleteById(id);
        return productsRepository.findAll();
    }

    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> updateProduct(Long id, ProductDTO productDTO) {
        Optional<Product> o = productsRepository.findById(id);
        if (o.isPresent()) {
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
    public List<Product> addProductTag(Long productId, Long tagId) {
        Optional<Product> p = productsRepository.findById(productId);
        Optional<TagsForProducts> t = tagsForProductsRepository.findById(tagId);
        if (p.isEmpty() || t.isEmpty())
            return null;
        p.get().addTag(t.get());
        return productsRepository.findAll();
    }

    @Transactional
    @Secured(value = "ADMIN")
    public List<Product> delProductTag(Long productId, Long tagId) {
        Optional<Product> p = productsRepository.findById(productId);
        Optional<TagsForProducts> t = tagsForProductsRepository.findById(tagId);
        if (p.isEmpty() || t.isEmpty())
            return null;
        p.get().delTag(t.get());
        return productsRepository.findAll();
    }
}
