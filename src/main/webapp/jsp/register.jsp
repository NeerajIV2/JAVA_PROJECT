<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="auth-container">
        <div class="auth-card">
            <h1>Register</h1>
            
            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/users">
                <input type="hidden" name="action" value="register">
                
                <div class="form-group">
                    <label for="username">Username *</label>
                    <input type="text" id="username" name="username" required 
                           placeholder="Choose a username">
                </div>

                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" id="email" name="email" required 
                           placeholder="Enter your email">
                </div>

                <div class="form-group">
                    <label for="fullName">Full Name *</label>
                    <input type="text" id="fullName" name="fullName" required 
                           placeholder="Enter your full name">
                </div>

                <div class="form-group">
                    <label for="password">Password *</label>
                    <input type="password" id="password" name="password" required 
                           placeholder="Choose a password">
                </div>

                <button type="submit" class="btn btn-primary btn-block">Register</button>
            </form>

            <div class="auth-footer">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/users?action=login">Login here</a></p>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

