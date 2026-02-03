package com.recipeapp.recipe_backend.service;

import com.recipeapp.recipe_backend.entity.RecommendationRequest;
import com.recipeapp.recipe_backend.entity.RecommendationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class RecommendationService {

    private final WebClient webClient;

    public RecommendationService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8000")
                .build();
    }

    public List<RecommendationResponse> getRecommendations(String query, int topK) {

        RecommendationRequest request =
                new RecommendationRequest(query, topK);

        return webClient.post()
                .uri("/recommend")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(RecommendationResponse.class)
                .collectList()
                .block();
    }
}

