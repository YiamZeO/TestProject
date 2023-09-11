package com.example.usersdb.repositories.specs;

import com.example.usersdb.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UsersSpecs {
    public static Specification<User> nameIs(String name) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.equal(root.get("name"), name);
        };
    }

    public static Specification<User> ageGrThenOrEq(Long age) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.greaterThanOrEqualTo(root.get("age"), age);
        };
    }

    public static Specification<User> ageLeThenOrEq(Long age) {
        return (root, criteriaQuery, criteriaBuldier) -> {
            return criteriaBuldier.lessThanOrEqualTo(root.get("age"), age);
        };
    }
}
