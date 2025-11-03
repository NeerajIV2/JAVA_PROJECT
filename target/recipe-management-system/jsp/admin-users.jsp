<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<div class="container">
    <h1 class="page-title">Manage Users</h1>
    <a href="${pageContext.request.contextPath}/admin" class="btn btn-secondary">Back to Dashboard</a>

    <div class="admin-table-container">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Full Name</th>
                    <th>Role</th>
                    <th>Favorites</th>
                    <th>Bookmarks</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.fullName}</td>
                        <td>
                            <span class="role-badge ${user.role == 'ADMIN' ? 'admin' : 'user'}">
                                ${user.role}
                            </span>
                        </td>
                        <td>${user.favoriteRecipeIds.size()}</td>
                        <td>${user.bookmarkedRecipeIds.size()}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="footer.jsp" %>

