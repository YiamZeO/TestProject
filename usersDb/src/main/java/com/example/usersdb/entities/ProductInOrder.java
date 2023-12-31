package com.example.usersdb.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "products_in_order", schema = "users_schema")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"order", "product"})
public class ProductInOrder {
    @Id
    @EmbeddedId
    private ProductInOrderCompKey productInOrderCompKey;
    private Long productCount;
    private Long costInOrder;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @MapsId("orderId")
    @JsonIgnoreProperties("productsInOrder")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @MapsId("productId")
    @JsonIgnoreProperties("productsInOrders")
    private Product product;

    public ProductInOrder(Long productCount, Long productId, Long productCost){
        this.productCount = productCount;
        this.productInOrderCompKey = new ProductInOrderCompKey(productId);
        this.costInOrder = productCost;
    }
}
