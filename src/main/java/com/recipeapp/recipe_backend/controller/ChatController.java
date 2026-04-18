package com.recipeapp.recipe_backend.controller;

import com.recipeapp.recipe_backend.dto.chatbox.ChatMessageRequest;
import com.recipeapp.recipe_backend.dto.chatbox.ChatMessageResponse;
import com.recipeapp.recipe_backend.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat.send")
    public void sendMessage(
            ChatMessageRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        System.out.println("🔥 MESSAGE RECEIVED: " + request.getMessage()+ request.getIsGenerated());

        //String sessionId = headerAccessor.getSessionId();

        ChatMessageResponse response = chatService.processMessage(request);
        System.out.println("🔥 Response: " + response);
        String destination = "/queue/chat-" + request.getRecipeId();

        messagingTemplate.convertAndSendToUser(
                String.valueOf(request.getUserId()),
                destination,
                response
        );
    }
}
