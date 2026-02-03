package com.recipeapp.recipe_backend.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(columnDefinition = "TEXT")
    private String directions;

    @Column(columnDefinition = "TEXT")
    private String nutrition;

    @Column(columnDefinition = "TEXT")
    private String imgSrc;

    private String prepTime;
    private String cookTime;
    private String totalTime;
    private String servings;

    private String url;
}
