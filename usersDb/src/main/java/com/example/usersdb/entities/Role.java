package com.example.usersdb.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "users_schema")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    @Column
    private Long id;
    @Column
    private String name;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("roles")
    private Set<User> role_users = new HashSet<>();

    public Role(String name){
        this.name = name;
    }
}
