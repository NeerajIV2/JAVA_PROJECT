package com.recipe.servlet;

import com.recipe.dao.RecipeDAO;
import com.recipe.model.Recipe;
import com.recipe.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportServlet extends HttpServlet {
    private RecipeDAO recipeDAO = new RecipeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("users?action=login");
            return;
        }

        String recipeId = request.getParameter("id");
        String format = request.getParameter("format");
        
        if (format == null) {
            format = "xml";
        }

        if (recipeId != null && !recipeId.isEmpty()) {
            exportSingleRecipe(recipeId, format, response);
        } else {
            exportAllRecipes(user, format, response);
        }
    }

    private void exportSingleRecipe(String recipeId, String format, HttpServletResponse response)
            throws IOException {
        Recipe recipe = recipeDAO.getRecipeById(recipeId);
        if (recipe == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if ("xml".equals(format)) {
            exportRecipeXML(recipe, response);
        } else if ("text".equals(format)) {
            exportRecipeText(recipe, response);
        } else {
            exportRecipeXML(recipe, response);
        }
    }

    private void exportAllRecipes(User user, String format, HttpServletResponse response)
            throws IOException {
        List<Recipe> recipes;

        if (user.isAdmin()) {
            recipes = recipeDAO.getAllRecipes();
        } else {
            recipes = recipeDAO.getRecipesByUser(user.getId());
        }

        if ("xml".equals(format)) {
            exportRecipesXML(recipes, response);
        } else if ("text".equals(format)) {
            exportRecipesText(recipes, response);
        } else {
            exportRecipesXML(recipes, response);
        }
    }

    private void exportRecipeXML(Recipe recipe, HttpServletResponse response) throws IOException {
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", 
                "attachment; filename=\"recipe_" + recipe.getId() + ".xml\"");

        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<recipe id=\"" + escapeXML(recipe.getId()) + "\">");
        out.println("  <title>" + escapeXML(recipe.getTitle()) + "</title>");
        out.println("  <description>" + escapeXML(recipe.getDescription()) + "</description>");
        out.println("  <cuisineType>" + escapeXML(recipe.getCuisineType()) + "</cuisineType>");
        out.println("  <difficultyLevel>" + escapeXML(recipe.getDifficultyLevel()) + "</difficultyLevel>");
        out.println("  <preparationTime>" + recipe.getPreparationTime() + "</preparationTime>");
        out.println("  <cookingTime>" + recipe.getCookingTime() + "</cookingTime>");
        out.println("  <servings>" + recipe.getServings() + "</servings>");
        out.println("  <category>" + escapeXML(recipe.getCategory()) + "</category>");
        out.println("  <authorName>" + escapeXML(recipe.getAuthorName()) + "</authorName>");
        out.println("  <averageRating>" + recipe.getAverageRating() + "</averageRating>");
        
        out.println("  <ingredients>");
        for (var ingredient : recipe.getIngredients()) {
            out.println("    <ingredient>");
            out.println("      <name>" + escapeXML(ingredient.getName()) + "</name>");
            out.println("      <quantity>" + ingredient.getQuantity() + "</quantity>");
            out.println("      <unit>" + escapeXML(ingredient.getUnit()) + "</unit>");
            if (ingredient.getNotes() != null) {
                out.println("      <notes>" + escapeXML(ingredient.getNotes()) + "</notes>");
            }
            out.println("    </ingredient>");
        }
        out.println("  </ingredients>");

        out.println("  <preparationSteps>");
        for (String step : recipe.getPreparationSteps()) {
            out.println("    <step>" + escapeXML(step) + "</step>");
        }
        out.println("  </preparationSteps>");

        out.println("  <tags>");
        for (String tag : recipe.getTags()) {
            out.println("    <tag>" + escapeXML(tag) + "</tag>");
        }
        out.println("  </tags>");

        out.println("</recipe>");
        out.flush();
    }

    private void exportRecipesXML(List<Recipe> recipes, HttpServletResponse response) throws IOException {
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", 
                "attachment; filename=\"recipes_export_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xml\"");

        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<recipes>");

        for (Recipe recipe : recipes) {
            out.println("  <recipe id=\"" + escapeXML(recipe.getId()) + "\">");
            out.println("    <title>" + escapeXML(recipe.getTitle()) + "</title>");
            out.println("    <description>" + escapeXML(recipe.getDescription()) + "</description>");
            out.println("    <cuisineType>" + escapeXML(recipe.getCuisineType()) + "</cuisineType>");
            out.println("    <category>" + escapeXML(recipe.getCategory()) + "</category>");
            out.println("    <authorName>" + escapeXML(recipe.getAuthorName()) + "</authorName>");
            out.println("  </recipe>");
        }

        out.println("</recipes>");
        out.flush();
    }

    private void exportRecipeText(Recipe recipe, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", 
                "attachment; filename=\"recipe_" + recipe.getId() + ".txt\"");

        PrintWriter out = response.getWriter();
        out.println("=".repeat(60));
        out.println(recipe.getTitle().toUpperCase());
        out.println("=".repeat(60));
        out.println();
        out.println("Description: " + recipe.getDescription());
        out.println("Cuisine Type: " + recipe.getCuisineType());
        out.println("Difficulty: " + recipe.getDifficultyLevel());
        out.println("Preparation Time: " + recipe.getPreparationTime() + " minutes");
        out.println("Cooking Time: " + recipe.getCookingTime() + " minutes");
        out.println("Total Time: " + recipe.getTotalTime() + " minutes");
        out.println("Servings: " + recipe.getServings());
        out.println("Category: " + recipe.getCategory());
        out.println("Author: " + recipe.getAuthorName());
        out.println("Rating: " + String.format("%.1f", recipe.getAverageRating()) + " (" + 
                recipe.getTotalRatings() + " ratings)");
        out.println();
        
        out.println("INGREDIENTS:");
        out.println("-".repeat(60));
        for (var ingredient : recipe.getIngredients()) {
            out.println("  • " + ingredient);
        }
        out.println();

        out.println("PREPARATION STEPS:");
        out.println("-".repeat(60));
        int stepNum = 1;
        for (String step : recipe.getPreparationSteps()) {
            out.println(stepNum + ". " + step);
            stepNum++;
        }
        out.println();

        if (!recipe.getTags().isEmpty()) {
            out.println("Tags: " + String.join(", ", recipe.getTags()));
        }
        out.println();
        out.println("=".repeat(60));

        out.flush();
    }

    private void exportRecipesText(List<Recipe> recipes, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", 
                "attachment; filename=\"recipes_export_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt\"");

        PrintWriter out = response.getWriter();
        out.println("RECIPE CATALOG");
        out.println("=".repeat(60));
        out.println("Total Recipes: " + recipes.size());
        out.println("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        out.println();

        for (Recipe recipe : recipes) {
            out.println("-".repeat(60));
            out.println(recipe.getTitle());
            out.println("  Category: " + recipe.getCategory() + " | Cuisine: " + recipe.getCuisineType());
            out.println("  Author: " + recipe.getAuthorName() + " | Rating: " + 
                    String.format("%.1f", recipe.getAverageRating()));
            out.println();
        }

        out.flush();
    }

    private String escapeXML(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}

