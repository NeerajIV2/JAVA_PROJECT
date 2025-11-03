<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="error-container">
        <h1>Oops! Something went wrong</h1>
        <p>We're sorry, but an error has occurred.</p>
        <a href="${pageContext.request.contextPath}/recipes" class="btn btn-primary">Go Home</a>
    </div>
</div>

<%@ include file="footer.jsp" %>

