package com.example.usersdb.entities;

import com.example.usersdb.DTOs.ProductDTO;
import com.example.usersdb.DTOs.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "products", schema = "users_schema")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;
    private Date date;
    @Column
    private String description;
    @Column
    private Long cost;
    @Column
    private Long quality;
    public Product(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.date = productDTO.getDate();
        this.description = productDTO.getDescription();
        this.cost = productDTO.getCost();
        this.quality = productDTO.getQuality();
    }
}
