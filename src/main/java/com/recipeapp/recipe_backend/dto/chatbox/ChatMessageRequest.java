package com.recipeapp.recipe_backend.dto.chatbox;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ChatMessageRequest {
    private Long userId;
    private Long recipeId;
    private Boolean isGenerated;
    private String message;
}