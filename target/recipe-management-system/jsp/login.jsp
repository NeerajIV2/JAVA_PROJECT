<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="auth-container">
        <div class="auth-card">
            <h1>Login</h1>
            
            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/users">
                <input type="hidden" name="action" value="login">
                
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" required 
                           placeholder="Enter your username">
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required 
                           placeholder="Enter your password">
                </div>

                <button type="submit" class="btn btn-primary btn-block">Login</button>
            </form>

            <div class="auth-footer">
                <p>Don't have an account? <a href="${pageContext.request.contextPath}/users?action=register">Register here</a></p>
                <p><small>Default admin: username: <strong>admin</strong>, password: <strong>admin123</strong></small></p>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

