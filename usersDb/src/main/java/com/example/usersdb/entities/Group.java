package com.example.usersdb.entities;

import com.example.usersdb.dto.GroupDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "groups", schema = "users_schema")
@Getter
@Setter
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private Long rating;
    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("groups")
    private Set<User> group_users;

    public Group(GroupDTO groupDTO) {
        this.name = groupDTO.getName();
        this.rating = groupDTO.getRating();
    }
}
