package com.recipeapp.recipe_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "user_preferences")
public class UserPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "dietary_types")
    private List<String> dietaryTypes;
    private List<String> allergies;
    @Column(name = "disliked_ingredients")
    private List<String> dislikedIngredients;
    @Column(name = "taste_preferences")
    private String tastePreferences;
    @Column(name = "flavour_preferences")
    private List<String> flavourPreferences;
}
