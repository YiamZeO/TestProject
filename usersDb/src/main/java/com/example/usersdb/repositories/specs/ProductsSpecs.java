package com.example.usersdb.repositories.specs;

import com.example.usersdb.entities.Product;
import com.example.usersdb.entities.TagsForProducts;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ProductsSpecs {
    public static Specification<Product> nameIs(String name) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.equal(root.get("name"), name);
        };
    }

    public static Specification<Product> costGrThenOrEq(Long cost) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.greaterThanOrEqualTo(root.get("cost"), cost);
        };
    }

    public static Specification<Product> costLeThenOrEq(Long cost) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.lessThanOrEqualTo(root.get("cost"), cost);
        };
    }

    public static Specification<Product> qualityGrThenOrEq(Long quality) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.greaterThanOrEqualTo(root.get("quality"), quality);
        };
    }

    public static Specification<Product> qualityLeThenOrEq(Long quality) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.lessThanOrEqualTo(root.get("quality"), quality);
        };
    }

    public static Specification<Product> dateGrThenOrEq(Date date) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.greaterThanOrEqualTo(root.get("date"), date);
        };
    }

    public static Specification<Product> dateLeThenOrEq(Date date) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.lessThanOrEqualTo(root.get("date"), date);
        };
    }

    public static Specification<Product> descriptionContain(String str) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.like(root.get("description"), "%" + str + "%");
        };
    }

    public static Specification<Product> isContainTag(TagsForProducts tag) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.isMember(tag, root.get("tags"));
        };
    }
}
