<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student List - MVC</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        
        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 32px;
        }
        
        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-style: italic;
        }
        
        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-weight: 500;
        }
        
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 24px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 500;
            transition: all 0.3s;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        
        .btn-danger {
            background-color: #dc3545;
            color: white;
            padding: 8px 16px;
            font-size: 13px;
        }
        
        .btn-danger:hover {
            background-color: #c82333;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            font-weight: 600;
            text-transform: uppercase;
            font-size: 13px;
            letter-spacing: 0.5px;
        }
        
        tbody tr {
            transition: background-color 0.2s;
        }
        
        tbody tr:hover {
            background-color: #f8f9fa;
        }
        
        .actions {
            display: flex;
            gap: 10px;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        
        .empty-state-icon {
            font-size: 64px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📚 Student Management System</h1>
        <p class="subtitle">MVC Pattern with Jakarta EE & JSTL</p>
        
        <form method="get" action="student" style="margin-bottom:20px; display:flex; gap:10px; align-items:center;">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" placeholder="Search by code, name, email, or major"
                   value="${keyword != null ? keyword: ''}"
                   style="padding:10px; border-radius:5px; border:1px solid #ccc; flex:1;">
            <button type="submit" class="btn btn-primary">🔍 Search</button>
            <c:if test="${keyword != null && !keyword.isEmpty()}">
                <a href="student?action=list" class="btn btn-secondary">✖ Show All</a>
            </c:if>
        </form>
        <c:if test="${keyword != null && !keyword.isEmpty()}">
            <div class="message" style="background-color:#e2e3ff; color:#1a1aff; border:1px solid #b3b3ff;">
               Search result for: <strong>${keyword}</strong> 
            </div>
        </c:if>
                   
        <!-- Success Message -->
        <c:if test="${not empty param.message}">
            <div class="message success">
                ✅ ${param.message}
            </div>
        </c:if>
        
        <!-- Error Message -->
        <c:if test="${not empty param.error}">
            <div class="message error">
                ❌ ${param.error}
            </div>
        </c:if>
        
        <!-- Add New Student Button -->
        <div style="margin-bottom: 20px;">
            <a href="student?action=new" class="btn btn-primary">
                ➕ Add New Student
            </a>
        </div>
        
        <!-- Major Filtering -->
        <div class="filter-box" style="margin-bottom: 20px;">
            <form action="student" method="get" style="display:flex; gap:10px; align-items:center;">
                <input type="hidden" name="action" value="filter">
                <label><strong>Filter by Major:</strong></label>
                <select name="major" style="padding:8px; border-radius:5px; border:1px solid #ccc;">
                    <option value="">All Majors</option>
                    <option value="Computer Science" 
                            ${selectedMajor == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                    <option value="Information Technology" 
                            ${selectedMajor == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                    <option value="Software Engineering"
                            ${selectedMajor == 'Software Engineering' ? 'selected' : ''}>Software Engineering</option>
                    <option value="Business Administration"
                            ${selectedMajor == 'Business Administration' ? 'selected' : ''}>Business Administration</option>
                </select>
                <button type="submit" class="btn btn-primary">Apply Filter</button>
                
                <c:if test="${not empty selectedMajor}">
                    <a href="student?action=list" class="btn btn-secondary">Clear Filter</a>
                </c:if> 
            </form>  
        </div>                  
        
        <!-- Student Table -->
        <c:choose>
            <c:when test="${not empty students}">
                <table>
                    <thead>
                        <tr>
                            <th><a href="student?action=sort&sortBy=id&order=${sortBy == 'id' && order == 'asc' ? 'desc' : 'asc'}">ID
                                    <c:if test="${sortBy == 'id'}">${order == 'asc' ? '▲' : '▼'}</c:if></a></th>
                            <th><a href="student?action=sort&sortBy=student_code&order=${sortBy == 'student_code' && order == 'asc' ? 'desc' : 'asc'}">Code
                                    <c:if test="${sortBy == 'student_code'}">${order == 'asc' ? '▲' : '▼'}</c:if></a></th>
                            <th><a href="student?action=sort&sortBy=full_name&order=${sortBy == 'full_name' && order == 'asc' ? 'desc' : 'asc'}">Name
                                    <c:if test="${sortBy == 'full_name'}">${order == 'asc' ? '▲' : '▼'}</c:if></a></th>
                            <th><a href="student?action=sort&sortBy=email&order=${sortBy == 'email' && order == 'asc' ? 'desc' : 'asc'}">Email
                                    <c:if test="${sortBy == 'email'}">${order == 'asc' ? '▲' : '▼'}</c:if></a></th>
                            <th><a href="student?action=sort&sortBy=major&order=asc">Major
                                    <c:if test="${sortBy == 'major'}">${order == 'asc' ? '▲' : '▼'}</c:if></a></th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="student" items="${students}">
                            <tr>
                                <td>${student.id}</td>
                                <td><strong>${student.studentCode}</strong></td>
                                <td>${student.fullName}</td>
                                <td>${student.email}</td>
                                <td>${student.major}</td>
                                <td>
                                    <div class="actions">
                                        <a href="student?action=edit&id=${student.id}" class="btn btn-secondary">
                                            ✏️ Edit
                                        </a>
                                        <a href="student?action=delete&id=${student.id}" 
                                           class="btn btn-danger"
                                           onclick="return confirm('Are you sure you want to delete this student?')">
                                            🗑️ Delete
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <h3>No students found</h3>
                    <p>Start by adding a new student</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
        <!-- Pagination UI --> 
        <div class="pagination" style="margin-top: 20px; text-align:center;">
            <!-- Previous button -->
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <a href="student?action=list&page=${currentPage - 1}" class="page-btn">« Previous</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled-btn">« Previous</span>
                </c:otherwise>
            </c:choose>
            <!-- Page numbers -->
            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <span class="current-page">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="student?action=list&page=${i}" class="page-btn">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <!-- Next button -->
            <c:choose>
                <c:when test="${currentPage < totalPages}">
                    <a href="student?action=list&page=${currentPage + 1}" class="page-btn">Next »</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled-btn">Next »</span>
                </c:otherwise>
            </c:choose>
        </div>
        <p style="text-align:center; margin-top:10px;">
            Showing page <strong>${currentPage}</strong> of <strong>${totalPages}</strong>
        </p>
</body>
</html>
