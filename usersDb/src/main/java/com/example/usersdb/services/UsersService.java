package com.example.usersdb.services;

import com.example.usersdb.DTOs.UserDTO;
import com.example.usersdb.entities.Group;
import com.example.usersdb.entities.Role;
import com.example.usersdb.entities.User;
import com.example.usersdb.repositories.GroupsRepository;
import com.example.usersdb.repositories.RolesRepository;
import com.example.usersdb.repositories.UsersRepository;
import com.example.usersdb.repositories.specs.UsersSpecs;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Setter
public class UsersService {
    private UsersRepository usersRepository;
    private RolesRepository rolesRepository;
    private GroupsRepository groupsRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, RolesRepository rolesRepository, GroupsRepository groupsRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.groupsRepository = groupsRepository;
    }

    public List<User> findAllUsers(){
        return usersRepository.findAll();
    }
    @Transactional
    public List<User> saveUser(UserDTO userDTO){
        usersRepository.save(new User(userDTO));
        return usersRepository.findAll();
    }
    @Transactional
    public List<User> deleteById(Long id){
        usersRepository.deleteById(id);
        return usersRepository.findAll();
    }
    @Transactional
    public List<User> updateUser(Long id, UserDTO userDTO){
        Optional<User> o = usersRepository.findById(id);
        if (o.isPresent()){
            User u = o.get();
            u.setName(userDTO.getName());
            u.setAge(userDTO.getAge());
            usersRepository.save(u);
        }
        return usersRepository.findAll();
    }
    public List<User> findByCriteria(String name, Long minAge, Long maxAge){
        Specification<User> spec = Specification.where(null);
        spec = spec.and(UsersSpecs.nameIs(name));
        spec = spec.and(UsersSpecs.ageGrThenOrEq(minAge));
        spec = spec.and(UsersSpecs.ageLeThenOrEq(maxAge));
        return usersRepository.findAll(spec);
    }
    @Transactional
    public List<User> delUserRole(Long userId, Long roleId){
        Optional<User> u = usersRepository.findById(userId);
        Optional<Role> r = rolesRepository.findById(roleId);
        if (u.isEmpty() || r.isEmpty())
            return (List<User>) null;
        u.get().removeRole(r.get());
        return usersRepository.findAll();
    }
    @Transactional
    public List<User> addUserRole(Long userId, Long roleId){
        Optional<User> u = usersRepository.findById(userId);
        Optional<Role> r = rolesRepository.findById(roleId);
        if (u.isEmpty() || r.isEmpty())
            return (List<User>) null;
        u.get().addRole(r.get());
        return usersRepository.findAll();
    }
    @Transactional
    public List<User> delUserGroup(Long userId, Long groupId){
        Optional<User> u = usersRepository.findById(userId);
        Optional<Group> g = groupsRepository.findById(groupId);
        if (u.isEmpty() || g.isEmpty())
            return (List<User>) null;
        u.get().removeGroup(g.get());
        return usersRepository.findAll();
    }
    @Transactional
    public List<User> addUserGroup(Long userId, Long groupId){
        Optional<User> u = usersRepository.findById(userId);
        Optional<Group> g = groupsRepository.findById(groupId);
        if (u.isEmpty() || g.isEmpty())
            return (List<User>) null;
        u.get().addGroup(g.get());
        return usersRepository.findAll();
    }
}
