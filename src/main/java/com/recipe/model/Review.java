package com.recipe.model;

import java.io.Serializable;

public class Review implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String recipeId;
    private String userId;
    private String username;
    private int rating; // 1-5
    private String comment;
    private String createdAt;

    public Review() {
    }

    public Review(String recipeId, String userId, String username, int rating, String comment) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

