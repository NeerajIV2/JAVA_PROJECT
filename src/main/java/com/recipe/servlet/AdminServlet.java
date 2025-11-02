package com.recipe.servlet;

import com.recipe.dao.RecipeDAO;
import com.recipe.model.Recipe;
import com.recipe.model.User;
import com.recipe.util.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class AdminServlet extends HttpServlet {
    private RecipeDAO recipeDAO = new RecipeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            response.sendRedirect("users?action=login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "dashboard";
        }

        switch (action) {
            case "dashboard":
                showDashboard(request, response);
                break;
            case "recipes":
                showRecipes(request, response);
                break;
            case "users":
                showUsers(request, response);
                break;
            case "approve":
                approveRecipe(request, response);
                break;
            default:
                showDashboard(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");

        if ("approve".equals(action)) {
            approveRecipe(request, response);
        } else if ("reject".equals(action)) {
            rejectRecipe(request, response);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Recipe> allRecipes = recipeDAO.getAllRecipes();
        List<Recipe> pendingRecipes = allRecipes.stream()
                .filter(r -> !r.isApproved())
                .collect(java.util.stream.Collectors.toList());
        List<Recipe> approvedRecipes = allRecipes.stream()
                .filter(Recipe::isApproved)
                .collect(java.util.stream.Collectors.toList());
        List<User> allUsers = UserManager.getAllUsers();

        request.setAttribute("totalRecipes", allRecipes.size());
        request.setAttribute("pendingRecipes", pendingRecipes.size());
        request.setAttribute("approvedRecipes", approvedRecipes.size());
        request.setAttribute("totalUsers", allUsers.size());
        request.setAttribute("recentRecipes", allRecipes.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .limit(5)
                .collect(java.util.stream.Collectors.toList()));

        request.getRequestDispatcher("/jsp/admin-dashboard.jsp").forward(request, response);
    }

    private void showRecipes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Recipe> recipes = recipeDAO.getAllRecipes();
        request.setAttribute("recipes", recipes);
        request.getRequestDispatcher("/jsp/admin-recipes.jsp").forward(request, response);
    }

    private void showUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = UserManager.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/jsp/admin-users.jsp").forward(request, response);
    }

    private void approveRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        try {
            Recipe recipe = recipeDAO.getRecipeById(id);
            if (recipe != null) {
                recipe.setApproved(true);
                recipeDAO.saveRecipe(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("admin?action=recipes");
    }

    private void rejectRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        try {
            recipeDAO.deleteRecipe(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("admin?action=recipes");
    }
}

