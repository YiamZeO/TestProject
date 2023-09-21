package com.example.usersdb.validators;

import com.example.usersdb.repositories.ProductsRepository;
import com.example.usersdb.validAnnotation.SumOfCostsLessThen1000;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SumOfCostsLessThen1000Validator implements ConstraintValidator<SumOfCostsLessThen1000, Long> {
    @Autowired
    ProductsRepository productsRepository;

    @Override
    public boolean isValid(Long cost, ConstraintValidatorContext constraintValidatorContext) {
        return cost != null && productsRepository.sumCost() + cost < 1000;
    }
}
