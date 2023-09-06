package com.example.usersdb.repositories;

import com.example.usersdb.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsRepository extends JpaRepository<Group, Long> {
}
