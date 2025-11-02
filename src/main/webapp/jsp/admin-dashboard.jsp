<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<div class="container">
    <h1 class="page-title">Admin Dashboard</h1>

    <div class="admin-stats">
        <div class="stat-card">
            <h3>Total Recipes</h3>
            <p class="stat-number">${totalRecipes}</p>
        </div>
        <div class="stat-card">
            <h3>Pending Approval</h3>
            <p class="stat-number">${pendingRecipes}</p>
        </div>
        <div class="stat-card">
            <h3>Approved Recipes</h3>
            <p class="stat-number">${approvedRecipes}</p>
        </div>
        <div class="stat-card">
            <h3>Total Users</h3>
            <p class="stat-number">${totalUsers}</p>
        </div>
    </div>

    <div class="admin-sections">
        <div class="admin-section">
            <h2>Quick Actions</h2>
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/admin?action=recipes" class="btn btn-primary">Manage Recipes</a>
                <a href="${pageContext.request.contextPath}/admin?action=users" class="btn btn-primary">Manage Users</a>
                <a href="${pageContext.request.contextPath}/recipes?action=form" class="btn btn-secondary">Create Recipe</a>
            </div>
        </div>

        <div class="admin-section">
            <h2>Recent Recipes</h2>
            <c:if test="${empty recentRecipes}">
                <p>No recipes yet.</p>
            </c:if>
            <ul class="recent-list">
                <c:forEach var="recipe" items="${recentRecipes}">
                    <li>
                        <a href="${pageContext.request.contextPath}/recipes?action=view&id=${recipe.id}">${recipe.title}</a>
                        <span class="recipe-status ${recipe.approved ? 'approved' : 'pending'}">
                            ${recipe.approved ? '✓ Approved' : '⏳ Pending'}
                        </span>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

