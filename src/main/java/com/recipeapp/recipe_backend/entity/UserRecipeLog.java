package com.recipeapp.recipe_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(
        name = "user_recipe_logs",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_user_recipe_log_user_recipe",
                columnNames = {"user_id", "recipe_id"}
        )
)
public class UserRecipeLog {
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
    private String rating;

    @Column(name = "cooked_at")
    private LocalDateTime cookedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;
}

