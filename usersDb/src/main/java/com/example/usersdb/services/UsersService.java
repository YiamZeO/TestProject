package com.example.usersdb.services;

import com.example.usersdb.dto.UserDTO;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Setter
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final GroupsRepository groupsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, RolesRepository rolesRepository, GroupsRepository groupsRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.groupsRepository = groupsRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public List<User> saveUser(UserDTO userDTO) {
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        usersRepository.save(new User(userDTO));
        return usersRepository.findAll();
    }

    @Transactional
    public List<User> deleteById(Long id) {
        usersRepository.deleteById(id);
        return usersRepository.findAll();
    }

    @Transactional
    public List<User> updateUser(Long id, UserDTO userDTO) {
        Optional<User> o = usersRepository.findById(id);
        if (o.isPresent()) {
            User u = o.get();
            u.setName(userDTO.getName());
            u.setAge(userDTO.getAge());
            u.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            usersRepository.save(u);
        }
        return usersRepository.findAll();
    }

    public List<User> findByCriteria(String name, Long minAge, Long maxAge) {
        Specification<User> spec = Specification.where(null);
        if (name != null && !name.isEmpty())
            spec = spec.and(UsersSpecs.nameIs(name));
        if (minAge != null && minAge >= 0)
            spec = spec.and(UsersSpecs.ageGrThenOrEq(minAge));
        if (maxAge != null && maxAge >= 0)
            spec = spec.and(UsersSpecs.ageLeThenOrEq(maxAge));
        return usersRepository.findAll(spec);
    }

    @Transactional
    public List<User> delUserRole(Long userId, Long roleId) {
        Optional<User> u = usersRepository.findById(userId);
        Optional<Role> r = rolesRepository.findById(roleId);
        if (u.isEmpty() || r.isEmpty())
            return null;
        u.get().removeRole(r.get());
        return usersRepository.findAll();
    }

    @Transactional
    public List<User> addUserRole(Long userId, Long roleId) {
        Optional<User> u = usersRepository.findById(userId);
        Optional<Role> r = rolesRepository.findById(roleId);
        if (u.isEmpty() || r.isEmpty())
            return null;
        u.get().addRole(r.get());
        return usersRepository.findAll();
    }

    @Transactional
    public List<User> delUserGroup(Long userId, Long groupId) {
        Optional<User> u = usersRepository.findById(userId);
        Optional<Group> g = groupsRepository.findById(groupId);
        if (u.isEmpty() || g.isEmpty())
            return null;
        u.get().removeGroup(g.get());
        return usersRepository.findAll();
    }

    @Transactional
    public List<User> addUserGroup(Long userId, Long groupId) {
        Optional<User> u = usersRepository.findById(userId);
        Optional<Group> g = groupsRepository.findById(groupId);
        if (u.isEmpty() || g.isEmpty())
            return null;
        u.get().addGroup(g.get());
        return usersRepository.findAll();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User u = usersRepository.findOneByName(name);
        if (u == null)
            throw new UsernameNotFoundException("Invalid username or password.");
        return new org.springframework.security.core.userdetails.User(u.getName(), u.getPassword(),
                mapRolesToAuthorities(u.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
