package com.recipeapp.recipe_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(
        name = "user_saved_recipe_logs",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_user_recipe_log_user_recipe",
                columnNames = {"user_id", "recipe_id"}
        )
)
public class UserSavedRecipeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(columnDefinition = "TEXT")
    private String notes;
}

