package com.example.usersdb.controllers;

import com.example.usersdb.entities.User;
import com.example.usersdb.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }
    @GetMapping
    public List<User> getAllUsers(){
        return usersService.findAllUsers();
    }
    @PostMapping("/addUser")
    public List<User> saveUser(@RequestParam String name, @RequestParam String age){
        return usersService.saveUser(name, age);
    }
    @DeleteMapping("/delUser")
    public List<User> deleteUser(@RequestParam String id){
        return usersService.deleteById(id);
    }
    @PostMapping("/uppUser")
    public List<User> updateUser(@RequestParam String id, @RequestParam String name, @RequestParam String age){
        return usersService.updateUser(id, name, age);
    }
    @GetMapping("findBy")
    public List<User> findByCriteria(@RequestParam(required = false)String name,
                                     @RequestParam(required = false)String minAge,
                                     @RequestParam(required = false)String maxAge){
        return usersService.findByCriteria(name, minAge, maxAge);
    }
    @PatchMapping("/changeUserRoles")
    public List<User> changeUserRoles(@RequestParam String action, @RequestParam String userId,
                                      @RequestParam String roleId){
        return usersService.changeRoles(action, userId, roleId);
    }
    @PatchMapping("/changeUserGroups")
    public List<User> changeUserGroups(@RequestParam String action, @RequestParam String userId,
                                       @RequestParam String groupId){
        return usersService.changeGroups(action, userId, groupId);
    }
}
