<%-- 
    Document   : change-password
    Created on : Nov 25, 2025, 5:50:34 PM
    Author     : User
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Change Password</title>
        <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .password-container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            width: 350px;
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        .message {
            padding: 10px 15px;
            margin-bottom: 15px;
            border-radius: 5px;
            font-weight: 500;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        form input {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }

        form button {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 5px;
            background: #667eea;
            color: white;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
        }

        form button:hover {
            background: #5a67d8;
        }
    </style>
    </head>
    <body>
        <div class="password-container">
        <h2>Change Password</h2>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>

        <!-- Success Message -->
        <c:if test="${not empty success}">
            <div class="message success">${success}</div>
            <div style="margin-top: 20px;">
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
                    🏠 Back to Dashboard
                </a>
    </div>
        </c:if>

        <form action="change-password" method="post">
            <input type="password" name="currentPassword" placeholder="Current Password" required>
            <input type="password" name="newPassword" placeholder="New Password" required>
            <input type="password" name="confirmPassword" placeholder="Confirm Password" required>
            <button type="submit">Change Password</button>
        </form>
    </div>
    </body>
</html>
