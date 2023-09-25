package com.example.usersdb.entities;

import com.example.usersdb.dto.OrderDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders", schema = "users_schema")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"orderUser", "productsInOrder"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("orders")
    private User orderUser;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("order")
    private Set<ProductInOrder> productsInOrder = new HashSet<>();

    public Order(OrderDTO orderDTO){
        this.address = orderDTO.getAddress();
        this.date = orderDTO.getDate();
    }

    @Transactional
    public void addProductInOrder(ProductInOrder productInOrder){
        this.productsInOrder.add(productInOrder);
        productInOrder.setOrder(this);
        productInOrder.getProductInOrderCompKey().setOrderId(this.id);
    }

    @Transactional
    public void delProductInOrder(ProductInOrder productInOrder){
        this.productsInOrder.remove(productInOrder);
        productInOrder.setOrder(null);
        productInOrder.getProductInOrderCompKey().setOrderId(null);
    }
}
