package com.example.usersdb.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "tags_for_products", schema = "users_schema")
@Getter
@Setter
@NoArgsConstructor
public class TagsForProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Long usage;
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("tags")
    private Set<Product> products;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TagsForProducts)) {
            return false;
        }
        TagsForProducts l = (TagsForProducts) obj;
        return this.getId().equals(l.getId());
    }
}
