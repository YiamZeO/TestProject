package com.example.usersdb.services;

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
    public List<User> saveUser(String name, String age){
        if (name.isEmpty() || age.isEmpty())
            return (List<User>) null;
        usersRepository.save(new User(name, Long.parseLong(age)));
        return usersRepository.findAll();
    }
    @Transactional
    public List<User> deleteById(String id){
        if (id.isEmpty())
            return (List<User>) null;
        usersRepository.deleteById(Long.parseLong(id));
        return usersRepository.findAll();
    }
    @Transactional
    public List<User> updateUser(String id, String name, String age){
        if (id.isEmpty() || name.isEmpty() || age.isEmpty())
            return (List<User>) null;
        Optional<User> o = usersRepository.findById(Long.parseLong(id));
        if (o.isPresent()){
            User u = o.get();
            u.setName(name);
            u.setAge(Long.parseLong(age));
            usersRepository.save(u);
        }
        return usersRepository.findAll();
    }
    public List<User> findByCriteria(String name, String minAge, String maxAge){
        Specification<User> spec = Specification.where(null);
        if (name != null && !name.isEmpty())
            spec = spec.and(UsersSpecs.nameIs(name));
        if(minAge != null && !minAge.isEmpty())
            spec = spec.and(UsersSpecs.ageGrThenOrEq(Long.parseLong(minAge)));
        if (maxAge != null && !maxAge.isEmpty())
            spec = spec.and(UsersSpecs.ageLeThenOrEq(Long.parseLong(maxAge)));
        return usersRepository.findAll(spec);
    }
    @Transactional
    public List<User> changeRoles(String action, String userId, String roleId){
        if (action.isEmpty() || (!action.equals("add") && !action.equals("del"))
                || userId.isEmpty() || roleId.isEmpty())
            return (List<User>) null;
        Optional<User> u = usersRepository.findById(Long.parseLong(userId));
        Optional<Role> r = rolesRepository.findById(Long.parseLong(roleId));
        if (u.isEmpty() || r.isEmpty())
            return (List<User>) null;
        User curUser = u.get();
        Role curRole = r.get();
        if (action.equals("add")){
            curUser.addRole(curRole);
        }
        else {
            curUser.removeRole(curRole);
        }
        return usersRepository.findAll();
    }
    @Transactional
    public List<User> changeGroups(String action, String userId, String groupId){
        if (action.isEmpty() || (!action.equals("add") && !action.equals("del"))
                || userId.isEmpty() || groupId.isEmpty())
            return (List<User>) null;
        Optional<User> u = usersRepository.findById(Long.parseLong(userId));
        Optional<Group> g = groupsRepository.findById(Long.parseLong(groupId));
        if (u.isEmpty() || g.isEmpty())
            return (List<User>) null;
        User curUser = u.get();
        Group curGroup = g.get();
        if (action.equals("add")){
            curUser.addGroup(curGroup);
        }
        else {
            curUser.removeGroup(curGroup);
        }
        return usersRepository.findAll();
    }
}
