package com.recipeapp.recipe_backend.repository;

import com.recipeapp.recipe_backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}

