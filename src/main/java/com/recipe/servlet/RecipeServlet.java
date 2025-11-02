package com.recipe.servlet;

import com.recipe.dao.RecipeDAO;
import com.recipe.model.Recipe;
import com.recipe.model.Ingredient;
import com.recipe.model.User;
import com.recipe.model.Review;
import com.recipe.util.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeServlet extends HttpServlet {
    private RecipeDAO recipeDAO = new RecipeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "view":
                viewRecipe(request, response);
                break;
            case "edit":
                editRecipe(request, response);
                break;
            case "form":
                showForm(request, response);
                break;
            case "delete":
                deleteRecipe(request, response);
                break;
            default:
                listRecipes(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            saveRecipe(request, response);
        } else if ("rate".equals(action)) {
            rateRecipe(request, response);
        } else if ("review".equals(action)) {
            addReview(request, response);
        } else {
            response.sendRedirect("recipes");
        }
    }

    private void listRecipes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String search = request.getParameter("search");
        String category = request.getParameter("category");
        String cuisineType = request.getParameter("cuisineType");
        String difficulty = request.getParameter("difficulty");

        List<Recipe> recipes;
        String userId = (user != null) ? user.getId() : null;

        if (user != null && user.isAdmin()) {
            recipes = recipeDAO.getAllRecipes();
        } else {
            recipes = recipeDAO.getRecipesForUser(userId);
        }

        if (search != null && !search.isEmpty()) {
            if (user != null && user.isAdmin()) {
                recipes = recipeDAO.searchRecipes(search);
            } else {
                recipes = recipeDAO.searchRecipes(search, userId);
            }
        } else if (category != null && !category.isEmpty()) {
            if (user != null && user.isAdmin()) {
                recipes = recipeDAO.filterByCategory(category);
            } else {
                recipes = recipeDAO.filterByCategory(category, userId);
            }
        } else if (cuisineType != null && !cuisineType.isEmpty()) {
            if (user != null && user.isAdmin()) {
                recipes = recipeDAO.filterByCuisineType(cuisineType);
            } else {
                recipes = recipeDAO.filterByCuisineType(cuisineType, userId);
            }
        } else if (difficulty != null && !difficulty.isEmpty()) {
            if (user != null && user.isAdmin()) {
                recipes = recipeDAO.filterByDifficulty(difficulty);
            } else {
                recipes = recipeDAO.filterByDifficulty(difficulty, userId);
            }
        }

        request.setAttribute("recipes", recipes);
        request.getRequestDispatcher("/jsp/recipe-list.jsp").forward(request, response);
    }

    private void viewRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        Recipe recipe = recipeDAO.getRecipeById(id);

        if (recipe == null) {
            response.sendRedirect("recipes");
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user has favorited/bookmarked this recipe
        if (user != null) {
            User currentUser = UserManager.getUserById(user.getId());
            request.setAttribute("isFavorite", currentUser.getFavoriteRecipeIds().contains(id));
            request.setAttribute("isBookmarked", currentUser.getBookmarkedRecipeIds().contains(id));
        }

        request.setAttribute("recipe", recipe);
        request.getRequestDispatcher("/jsp/recipe-detail.jsp").forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("users?action=login");
            return;
        }

        String id = request.getParameter("id");
        if (id != null) {
            Recipe recipe = recipeDAO.getRecipeById(id);
            if (recipe != null && (user.isAdmin() || recipe.getUserId().equals(user.getId()))) {
                request.setAttribute("recipe", recipe);
            }
        }

        request.getRequestDispatcher("/jsp/recipe-form.jsp").forward(request, response);
    }

    private void editRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showForm(request, response);
    }

    private void saveRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("users?action=login");
            return;
        }

        try {
            String id = request.getParameter("id");
            Recipe recipe;

            if (id != null && !id.isEmpty()) {
                recipe = recipeDAO.getRecipeById(id);
                if (recipe == null || (!user.isAdmin() && !recipe.getUserId().equals(user.getId()))) {
                    response.sendRedirect("recipes");
                    return;
                }
            } else {
                recipe = new Recipe();
                recipe.setId(com.recipe.util.XMLRecipeManager.generateId());
                recipe.setCreatedAt(java.time.LocalDateTime.now().toString());
            }

            recipe.setTitle(request.getParameter("title"));
            recipe.setDescription(request.getParameter("description"));
            recipe.setCuisineType(request.getParameter("cuisineType"));
            recipe.setDifficultyLevel(request.getParameter("difficultyLevel"));
            recipe.setPreparationTime(Integer.parseInt(request.getParameter("preparationTime")));
            recipe.setCookingTime(Integer.parseInt(request.getParameter("cookingTime")));
            recipe.setServings(Integer.parseInt(request.getParameter("servings")));
            recipe.setCategory(request.getParameter("category"));
            recipe.setUserId(user.getId());
            recipe.setAuthorName(user.getFullName());

            // Parse ingredients
            String[] ingredientNames = request.getParameterValues("ingredientName");
            String[] quantities = request.getParameterValues("ingredientQuantity");
            String[] units = request.getParameterValues("ingredientUnit");

            List<Ingredient> ingredients = new ArrayList<>();
            if (ingredientNames != null) {
                for (int i = 0; i < ingredientNames.length; i++) {
                    if (ingredientNames[i] != null && !ingredientNames[i].isEmpty()) {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setName(ingredientNames[i]);
                        ingredient.setQuantity(quantities != null && i < quantities.length && 
                                !quantities[i].isEmpty() ? Double.parseDouble(quantities[i]) : 0);
                        ingredient.setUnit(units != null && i < units.length ? units[i] : "");
                        ingredients.add(ingredient);
                    }
                }
            }
            recipe.setIngredients(ingredients);

            // Parse preparation steps
            String[] steps = request.getParameterValues("step");
            List<String> preparationSteps = new ArrayList<>();
            if (steps != null) {
                for (String step : steps) {
                    if (step != null && !step.trim().isEmpty()) {
                        preparationSteps.add(step);
                    }
                }
            }
            recipe.setPreparationSteps(preparationSteps);

            // Parse tags
            String tagsStr = request.getParameter("tags");
            List<String> tags = new ArrayList<>();
            if (tagsStr != null && !tagsStr.isEmpty()) {
                tags = Arrays.asList(tagsStr.split(",\\s*"));
            }
            recipe.setTags(tags);

            // If admin is saving, they can approve directly
            if (user.isAdmin()) {
                recipe.setApproved(true);
            }

            recipeDAO.saveRecipe(recipe);
            response.sendRedirect("recipes?action=view&id=" + recipe.getId());

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error saving recipe: " + e.getMessage());
            request.getRequestDispatcher("/jsp/recipe-form.jsp").forward(request, response);
        }
    }

    private void deleteRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("users?action=login");
            return;
        }

        String id = request.getParameter("id");
        Recipe recipe = recipeDAO.getRecipeById(id);

        if (recipe != null && (user.isAdmin() || recipe.getUserId().equals(user.getId()))) {
            try {
                recipeDAO.deleteRecipe(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (user.isAdmin()) {
            response.sendRedirect("admin?action=recipes");
        } else {
            response.sendRedirect("recipes");
        }
    }

    private void rateRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("users?action=login");
            return;
        }

        String recipeId = request.getParameter("recipeId");
        int rating = Integer.parseInt(request.getParameter("rating"));

        try {
            Recipe recipe = recipeDAO.getRecipeById(recipeId);
            if (recipe != null) {
                // Simple rating update - in production, you'd want to track individual user ratings
                int currentTotal = recipe.getTotalRatings();
                double currentAvg = recipe.getAverageRating();
                double newAvg = ((currentAvg * currentTotal) + rating) / (currentTotal + 1);
                
                recipe.setAverageRating(newAvg);
                recipe.setTotalRatings(currentTotal + 1);
                
                recipeDAO.saveRecipe(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("recipes?action=view&id=" + recipeId);
    }

    private void addReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("users?action=login");
            return;
        }

        String recipeId = request.getParameter("recipeId");
        String comment = request.getParameter("comment");
        int rating = Integer.parseInt(request.getParameter("rating"));

        try {
            Recipe recipe = recipeDAO.getRecipeById(recipeId);
            if (recipe != null) {
                Review review = new Review();
                review.setId("REVIEW_" + System.currentTimeMillis());
                review.setRecipeId(recipeId);
                review.setUserId(user.getId());
                review.setUsername(user.getFullName());
                review.setRating(rating);
                review.setComment(comment);
                review.setCreatedAt(java.time.LocalDateTime.now().toString());

                recipe.getReviews().add(review);
                
                // Update average rating
                int total = recipe.getTotalRatings() + 1;
                double currentAvg = recipe.getAverageRating();
                double newAvg = ((currentAvg * recipe.getTotalRatings()) + rating) / total;
                recipe.setAverageRating(newAvg);
                recipe.setTotalRatings(total);

                recipeDAO.saveRecipe(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("recipes?action=view&id=" + recipeId);
    }
}

