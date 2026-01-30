package com.recipeapp.recipe_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table
@Entity
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int servings;
    private int totalTime;
    private int prepTime;
    private int cookTime;
    private List<String> ingredients;
    private String description;
    private String directions;
    private String nutrition;
    private String imgSrc;
    private String url;
}
