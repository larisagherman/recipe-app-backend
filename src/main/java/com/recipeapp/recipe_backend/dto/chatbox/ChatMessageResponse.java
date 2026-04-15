package com.recipeapp.recipe_backend.dto.chatbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
    private String content;
    private String sender; // "user" or "ai"
}