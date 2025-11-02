package com.recipe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String role; // "USER" or "ADMIN"
    private List<String> favoriteRecipeIds;
    private List<String> bookmarkedRecipeIds;

    public User() {
        this.favoriteRecipeIds = new ArrayList<>();
        this.bookmarkedRecipeIds = new ArrayList<>();
        this.role = "USER";
    }

    public User(String username, String email, String password, String fullName) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getFavoriteRecipeIds() {
        return favoriteRecipeIds;
    }

    public void setFavoriteRecipeIds(List<String> favoriteRecipeIds) {
        this.favoriteRecipeIds = favoriteRecipeIds;
    }

    public List<String> getBookmarkedRecipeIds() {
        return bookmarkedRecipeIds;
    }

    public void setBookmarkedRecipeIds(List<String> bookmarkedRecipeIds) {
        this.bookmarkedRecipeIds = bookmarkedRecipeIds;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}

