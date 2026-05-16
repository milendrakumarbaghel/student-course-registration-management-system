<%@ page import="java.util.List" %>
<%@ page import="com.studentcourse.model.Registration" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    List<?> registrations = (List<?>) request.getAttribute("registrations");
%>
<html>
<head>
    <title>Registrations</title>
    <style>
        table { border-collapse: collapse; }
        th, td { border: 1px solid #999; padding: 6px; }
    </style>
</head>
<body>
<h2>Registration List</h2>

<p>
    <a href="<%=request.getContextPath()%>/dashboard">Dashboard</a> |
    <a href="<%=request.getContextPath()%>/registration/add">Add Registration</a> |
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
</p>

<table>
    <tr>
        <th>ID</th>
        <th>Student</th>
        <th>Course</th>
        <th>Date</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    <% if (registrations != null) {
        for (Object obj : registrations) {
            Registration r = (Registration) obj; %>
    <tr>
        <td><%= r.getRegistrationId() %></td>
        <td><%= r.getStudentName() %></td>
        <td><%= r.getCourseName() %></td>
        <td><%= r.getRegistrationDate() %></td>
        <td><%= r.getStatus() %></td>
        <td>
            <a href="<%=request.getContextPath()%>/registration/status?id=<%= r.getRegistrationId() %>&status=Active">Set Active</a>
            |
            <a href="<%=request.getContextPath()%>/registration/status?id=<%= r.getRegistrationId() %>&status=Completed">Set Completed</a>
            |
            <a href="<%=request.getContextPath()%>/registration/status?id=<%= r.getRegistrationId() %>&status=Cancelled">Set Cancelled</a>
            |
            <a href="<%=request.getContextPath()%>/registration/delete?id=<%= r.getRegistrationId() %>" onclick="return confirm('Delete this registration?');">Delete</a>
        </td>
    </tr>
    <%  }
    } %>
</table>
</body>
</html>
