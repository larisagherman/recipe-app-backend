package com.recipeapp.recipe_backend.service;

import com.recipeapp.recipe_backend.dto.chatbox.ChatMessageRequest;
import com.recipeapp.recipe_backend.dto.chatbox.ChatMessageResponse;
import com.recipeapp.recipe_backend.dto.chatbox.ChatRecipeDTO;
import com.recipeapp.recipe_backend.entity.GeneratedRecipe;
import com.recipeapp.recipe_backend.entity.Recipe;
import com.recipeapp.recipe_backend.mapper.ChatRecipeMapper;
import com.recipeapp.recipe_backend.repository.GeneratedRecipeRepository;
import com.recipeapp.recipe_backend.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatService {
    private final RecipeRepository recipeRepository;
    private final GeneratedRecipeRepository generatedRecipeRepository;
    private final GeminiService geminiService;
    private final ChatRecipeMapper chatRecipeMapper;
    public ChatMessageResponse processMessage(ChatMessageRequest request) {
        ChatRecipeDTO chatRecipe;
        if(request.getIsGenerated()){
            GeneratedRecipe generatedRecipe=generatedRecipeRepository.findById(request.getRecipeId()).orElseThrow();
            chatRecipe=chatRecipeMapper.generatedRecipeToChatRecipeDTO(generatedRecipe);
        }else{
            Recipe recipe = recipeRepository.findById(request.getRecipeId())
                    .orElseThrow();
            chatRecipe=chatRecipeMapper.recipeToChatRecipeDTO(recipe);
        }


        String prompt = geminiService.buildChatPrompt(chatRecipe, request.getMessage());

        String aiResponse = geminiService.askGemini(prompt);

        return new ChatMessageResponse(aiResponse, "ai");
    }
}
