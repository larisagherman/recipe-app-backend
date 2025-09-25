package com.recipeapp.recipe_backend.entity;

import jakarta.persistence.*;

@Table
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String description;
    String instructions;
}
