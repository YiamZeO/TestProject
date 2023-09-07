package com.example.usersdb.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "users_schema")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private Long age;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            schema = "users_schema",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnoreProperties("role_users")
    private Set<Role> roles;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_groups",
            schema = "users_schema",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    @JsonIgnoreProperties("group_users")
    private Set<Group> groups;
    public User(String name, Long age) {
        this.name = name;
        this.age = age;
    }
    @Transactional
    public void addRole(Role role){
        this.roles.add(role);
        role.getRole_users().add(this);
    }
    @Transactional
    public void removeRole(Role role){
        this.roles.remove(role);
        role.getRole_users().remove(this);
    }
    @Transactional
    public void addGroup(Group group){
        this.groups.add(group);
        group.getGroup_users().add(this);
    }
    @Transactional
    public void removeGroup(Group group){
        this.groups.remove(group);
        group.getGroup_users().remove(this);
    }
}
