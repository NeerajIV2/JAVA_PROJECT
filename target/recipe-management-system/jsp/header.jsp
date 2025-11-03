<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Object userObj = session.getAttribute("user");
    pageContext.setAttribute("user", userObj);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recipe Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="nav-brand">
                <a href="${pageContext.request.contextPath}/recipes">🍳 Recipe Manager</a>
            </div>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/recipes">Home</a>
                <c:if test="${not empty user}">
                    <a href="${pageContext.request.contextPath}/recipes?action=form">Add Recipe</a>
                    <a href="${pageContext.request.contextPath}/users?action=profile">Profile</a>
                    <c:if test="${user.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin">Admin</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/users?action=logout">Logout</a>
                </c:if>
                <c:if test="${empty user}">
                    <a href="${pageContext.request.contextPath}/users?action=login">Login</a>
                    <a href="${pageContext.request.contextPath}/users?action=register">Register</a>
                </c:if>
            </div>
        </div>
    </nav>
    <main class="main-content">

