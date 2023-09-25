package com.example.usersdb.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
public class ProductInOrderCompKey implements Serializable {
    private Long productId;
    private Long orderId;

    public ProductInOrderCompKey(Long productId){
        this.productId = productId;
    }
}
