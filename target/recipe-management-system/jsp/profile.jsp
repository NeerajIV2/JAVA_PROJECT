<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<c:if test="${empty user}">
    <div class="container">
        <div class="error-message">Please login to view your profile.</div>
        <a href="${pageContext.request.contextPath}/users?action=login" class="btn btn-primary">Login</a>
    </div>
</c:if>

<c:if test="${not empty user}">
<div class="container">
    <div class="profile-container">
        <div class="profile-header">
            <h1>My Profile</h1>
        </div>

        <div class="profile-info">
            <div class="profile-card">
                <h2>Account Information</h2>
                <div class="info-item">
                    <strong>Username:</strong> ${user.username}
                </div>
                <div class="info-item">
                    <strong>Email:</strong> ${user.email}
                </div>
                <div class="info-item">
                    <strong>Full Name:</strong> ${user.fullName}
                </div>
                <div class="info-item">
                    <strong>Role:</strong> ${user.role}
                </div>
            </div>

            <div class="profile-card">
                <h2>My Favorites</h2>
                <c:choose>
                    <c:when test="${empty user.favoriteRecipeIds}">
                        <p>No favorite recipes yet.</p>
                    </c:when>
                    <c:otherwise>
                        <p>You have ${user.favoriteRecipeIds.size()} favorite recipe(s).</p>
                        <a href="${pageContext.request.contextPath}/recipes" class="btn btn-primary">View Recipes</a>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="profile-card">
                <h2>Bookmarked Recipes</h2>
                <c:choose>
                    <c:when test="${empty user.bookmarkedRecipeIds}">
                        <p>No bookmarked recipes yet.</p>
                    </c:when>
                    <c:otherwise>
                        <p>You have ${user.bookmarkedRecipeIds.size()} bookmarked recipe(s).</p>
                        <a href="${pageContext.request.contextPath}/recipes" class="btn btn-primary">View Recipes</a>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="profile-card">
                <h2>My Recipes</h2>
                <a href="${pageContext.request.contextPath}/recipes?action=form" class="btn btn-primary">Create New Recipe</a>
                <a href="${pageContext.request.contextPath}/recipes" class="btn btn-secondary">View All Recipes</a>
            </div>

            <div class="profile-card">
                <h2>Export Data</h2>
                <a href="${pageContext.request.contextPath}/export" class="btn btn-outline">Export All My Recipes (XML)</a>
                <a href="${pageContext.request.contextPath}/export?format=text" class="btn btn-outline">Export All My Recipes (Text)</a>
            </div>
        </div>
    </div>
</div>
</c:if>

<%@ include file="footer.jsp" %>

