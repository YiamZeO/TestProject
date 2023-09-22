package com.example.usersdb.entities;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
public class ProductInOrderCompKey implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    public ProductInOrderCompKey(Long productId){
        this.productId = productId;
    }
}
