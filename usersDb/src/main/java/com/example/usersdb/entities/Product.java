package com.example.usersdb.entities;

import com.example.usersdb.dto.ProductDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    @Column
    private Date date;
    @Column
    private String description;
    @Column
    private Long cost;
    @Column
    private Long quality;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "products_tags",
            schema = "users_schema",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnoreProperties("products")
    private Set<TagsForProducts> tags = new HashSet<>();
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("product")
    private Set<ProductInOrder> productInOrders;

    public Product(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.date = productDTO.getDate();
        this.description = productDTO.getDescription();
        this.cost = productDTO.getCost();
        this.quality = productDTO.getQuality();
    }

    @Transactional
    public void addTag(TagsForProducts tag) {
        this.tags.add(tag);
        tag.getProducts().add(this);
        tag.setUsage(tag.getUsage() + 1);
    }

    @Transactional
    public void delTag(TagsForProducts tag) {
        this.tags.remove(tag);
        tag.getProducts().remove(this);
        tag.setUsage(tag.getUsage() - 1);
    }
}
