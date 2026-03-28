package com.recipeapp.recipe_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "weekly_recommendations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "week_start"})
})@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeeklyRecommendations {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Date weekStart;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "generated_recipe_id", nullable = false)
    private GeneratedRecipe recipe;
}
