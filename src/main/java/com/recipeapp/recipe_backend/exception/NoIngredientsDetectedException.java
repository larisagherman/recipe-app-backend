package com.recipeapp.recipe_backend.exception;


public class NoIngredientsDetectedException extends RuntimeException {

    public NoIngredientsDetectedException() {
        super("No ingredients detected in image");
    }

    public NoIngredientsDetectedException(String message) {
        super(message);
    }
}