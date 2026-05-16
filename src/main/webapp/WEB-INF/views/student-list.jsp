<%@ page import="java.util.List" %>
<%@ page import="com.studentcourse.model.Student" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    List<?> students = (List<?>) request.getAttribute("students");
    String error = (String) request.getAttribute("error");
%>
<html>
<head>
    <title>Students</title>
    <style>
        table { border-collapse: collapse; }
        th, td { border: 1px solid #999; padding: 6px; }
    </style>
</head>
<body>
<h2>Student List</h2>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<p>
    <a href="<%=request.getContextPath()%>/dashboard">Dashboard</a> |
    <a href="<%=request.getContextPath()%>/student/add">Add Student</a> |
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
</p>

<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Phone</th>
        <th>Age</th>
        <th>City</th>
        <th>Actions</th>
    </tr>
    <% if (students != null) {
        for (Object obj : students) {
            Student s = (Student) obj; %>
    <tr>
        <td><%= s.getStudentId() %></td>
        <td><%= s.getStudentName() %></td>
        <td><%= s.getEmail() %></td>
        <td><%= s.getPhone() %></td>
        <td><%= s.getAge() %></td>
        <td><%= s.getCity() %></td>
        <td>
            <a href="<%=request.getContextPath()%>/student/edit?id=<%= s.getStudentId() %>">Edit</a>
            |
            <a href="<%=request.getContextPath()%>/student/delete?id=<%= s.getStudentId() %>" onclick="return confirm('Delete this student?');">Delete</a>
        </td>
    </tr>
    <%  }
    } %>
</table>
</body>
</html>
