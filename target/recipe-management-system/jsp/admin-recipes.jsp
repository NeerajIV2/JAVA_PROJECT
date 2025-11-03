<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<div class="container">
    <h1 class="page-title">Manage Recipes</h1>
    <a href="${pageContext.request.contextPath}/admin" class="btn btn-secondary">Back to Dashboard</a>

    <div class="admin-table-container">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Category</th>
                    <th>Status</th>
                    <th>Rating</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="recipe" items="${recipes}">
                    <tr>
                        <td>${recipe.id}</td>
                        <td><a href="${pageContext.request.contextPath}/recipes?action=view&id=${recipe.id}">${recipe.title}</a></td>
                        <td>${recipe.authorName}</td>
                        <td>${recipe.category}</td>
                        <td>
                            <span class="recipe-status ${recipe.approved ? 'approved' : 'pending'}">
                                ${recipe.approved ? '✓ Approved' : '⏳ Pending'}
                            </span>
                        </td>
                        <td>${recipe.averageRating} (${recipe.totalRatings})</td>
                        <td class="action-buttons">
                            <a href="${pageContext.request.contextPath}/recipes?action=view&id=${recipe.id}" 
                               class="btn btn-sm btn-primary">View</a>
                            <c:if test="${!recipe.approved}">
                                <form method="post" action="${pageContext.request.contextPath}/admin" style="display: inline;">
                                    <input type="hidden" name="action" value="approve">
                                    <input type="hidden" name="id" value="${recipe.id}">
                                    <button type="submit" class="btn btn-sm btn-success">Approve</button>
                                </form>
                            </c:if>
                            <form method="post" action="${pageContext.request.contextPath}/recipes" style="display: inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${recipe.id}">
                                <button type="submit" class="btn btn-sm btn-danger" 
                                        onclick="return confirm('Are you sure you want to delete this recipe?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="footer.jsp" %>

