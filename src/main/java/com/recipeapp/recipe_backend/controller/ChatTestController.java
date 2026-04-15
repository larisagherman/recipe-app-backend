package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.dto.chatbox.ChatMessageRequest;
import com.recipeapp.recipe_backend.dto.chatbox.ChatMessageResponse;
import com.recipeapp.recipe_backend.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ChatTestController {

    private final ChatService chatService;

    public ChatTestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatMessageResponse testChat(@RequestBody ChatMessageRequest request) {
        return chatService.processMessage(request);
    }
}