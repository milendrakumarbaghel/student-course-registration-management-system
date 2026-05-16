<%@ page import="java.util.List" %>
<%@ page import="com.studentcourse.model.Course" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    List<?> courses = (List<?>) request.getAttribute("courses");
    String error = (String) request.getAttribute("error");
%>
<html>
<head>
    <title>Courses</title>
    <style>
        table { border-collapse: collapse; }
        th, td { border: 1px solid #999; padding: 6px; }
    </style>
</head>
<body>
<h2>Course List</h2>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<p>
    <a href="<%=request.getContextPath()%>/dashboard">Dashboard</a> |
    <a href="<%=request.getContextPath()%>/course/add">Add Course</a> |
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
</p>

<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Duration</th>
        <th>Fees</th>
        <th>Trainer</th>
        <th>Actions</th>
    </tr>
    <% if (courses != null) {
        for (Object obj : courses) {
            Course c = (Course) obj; %>
    <tr>
        <td><%= c.getCourseId() %></td>
        <td><%= c.getCourseName() %></td>
        <td><%= c.getDuration() %></td>
        <td><%= c.getFees() %></td>
        <td><%= c.getTrainerName() %></td>
        <td>
            <a href="<%=request.getContextPath()%>/course/edit?id=<%= c.getCourseId() %>">Edit</a>
            |
            <a href="<%=request.getContextPath()%>/course/delete?id=<%= c.getCourseId() %>" onclick="return confirm('Delete this course?');">Delete</a>
        </td>
    </tr>
    <%  }
    } %>
</table>
</body>
</html>
