package com.recipe.servlet;

import com.recipe.model.User;
import com.recipe.util.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "login";
        }

        switch (action) {
            case "login":
                showLogin(request, response);
                break;
            case "register":
                showRegister(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            case "profile":
                showProfile(request, response);
                break;
            default:
                showLogin(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            login(request, response);
        } else if ("register".equals(action)) {
            register(request, response);
        } else if ("favorite".equals(action)) {
            toggleFavorite(request, response);
        } else if ("bookmark".equals(action)) {
            toggleBookmark(request, response);
        } else {
            response.sendRedirect("users?action=login");
        }
    }

    private void showLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user != null) {
            response.sendRedirect("recipes");
            return;
        }

        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    private void showRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = UserManager.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("recipes");
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");

        if (UserManager.getUserByUsername(username) != null) {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }

        try {
            User user = new User(username, email, password, fullName);
            user.setId("USER_" + System.currentTimeMillis());

            UserManager.saveUser(user);

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("recipes");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error registering user: " + e.getMessage());
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("recipes");
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            response.sendRedirect("users?action=login");
            return;
        }

        // Get fresh user data
        User user = UserManager.getUserById(sessionUser.getId());
        request.setAttribute("user", user);
        request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
    }

    private void toggleFavorite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String recipeId = request.getParameter("recipeId");
            User user = UserManager.getUserById(sessionUser.getId());

            if (user.getFavoriteRecipeIds().contains(recipeId)) {
                user.getFavoriteRecipeIds().remove(recipeId);
            } else {
                user.getFavoriteRecipeIds().add(recipeId);
            }

            UserManager.saveUser(user);
            session.setAttribute("user", user);

            String redirect = request.getParameter("redirect");
            if (redirect != null) {
                response.sendRedirect(redirect);
            } else {
                response.sendRedirect("recipes");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void toggleBookmark(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String recipeId = request.getParameter("recipeId");
            User user = UserManager.getUserById(sessionUser.getId());

            if (user.getBookmarkedRecipeIds().contains(recipeId)) {
                user.getBookmarkedRecipeIds().remove(recipeId);
            } else {
                user.getBookmarkedRecipeIds().add(recipeId);
            }

            UserManager.saveUser(user);
            session.setAttribute("user", user);

            String redirect = request.getParameter("redirect");
            if (redirect != null) {
                response.sendRedirect(redirect);
            } else {
                response.sendRedirect("recipes");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

