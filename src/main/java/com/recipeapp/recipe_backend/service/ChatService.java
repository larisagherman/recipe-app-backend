package com.recipeapp.recipe_backend.service;

import com.recipeapp.recipe_backend.dto.chatbox.ChatMessageRequest;
import com.recipeapp.recipe_backend.dto.chatbox.ChatMessageResponse;
import com.recipeapp.recipe_backend.entity.Recipe;
import com.recipeapp.recipe_backend.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatService {
    private final RecipeRepository recipeRepository;
    private final GeminiService geminiService;
    public ChatMessageResponse processMessage(ChatMessageRequest request) {

        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow();

        String prompt = geminiService.buildChatPrompt(recipe, request.getMessage());

        String aiResponse = geminiService.askGemini(prompt);

        return new ChatMessageResponse(aiResponse, "ai");
    }
}
