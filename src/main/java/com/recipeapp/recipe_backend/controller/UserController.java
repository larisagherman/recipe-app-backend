package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.entity.User;
import com.recipeapp.recipe_backend.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UsersService usersService;

    @GetMapping
    public List<User> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return usersService.getUserById(id);
    }

}

