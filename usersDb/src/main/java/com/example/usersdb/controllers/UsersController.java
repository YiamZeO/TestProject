package com.example.usersdb.controllers;

import com.example.usersdb.DTOs.UserDTO;
import com.example.usersdb.entities.User;
import com.example.usersdb.services.UsersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    public List<User> saveUser(@RequestBody @Valid UserDTO userDTO){
        return usersService.saveUser(userDTO);
    }
    @DeleteMapping("/delUser")
    public List<User> deleteUser(@RequestParam @Min(0) Long id){
        return usersService.deleteById(id);
    }
    @PostMapping("/uppUser")
    public List<User> updateUser(@RequestParam @Min(0) Long id, @RequestBody @Valid UserDTO userDTO){
        return usersService.updateUser(id, userDTO);
    }
    @GetMapping("/findBy")
    public List<User> findByCriteria(@RequestParam(required = false) @NotBlank String name,
                                     @RequestParam(required = false) @Min(0) Long minAge,
                                     @RequestParam(required = false) @Min(0) Long maxAge){
        return usersService.findByCriteria(name, minAge, maxAge);
    }
    @DeleteMapping("/delUserRole")
    public List<User> deleteUserRole(@RequestParam @Min(0) Long userId,
                                      @RequestParam @Min(0) Long roleId){
        return usersService.delUserRole(userId, roleId);
    }
    @PostMapping("/addUserRole")
    public List<User> addUserRole(@RequestParam @Min(0) Long userId,
                                     @RequestParam @Min(0) Long roleId){
        return usersService.addUserRole(userId, roleId);
    }
    @DeleteMapping("/delUserGroup")
    public List<User> deleteUserGroup(@RequestParam @Min(0) Long userId,
                                     @RequestParam @Min(0) Long groupId){
        return usersService.delUserGroup(userId, groupId);
    }
    @PostMapping("/addUserGroup")
    public List<User> addUserGroup(@RequestParam @Min(0) Long userId,
                                  @RequestParam @Min(0) Long groupId){
        return usersService.addUserGroup(userId, groupId);
    }
}
