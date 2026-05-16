<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
<h2>Welcome, <%= request.getAttribute("loggedInUser") %></h2>

<p>Total Students: <strong><%= request.getAttribute("studentCount") %></strong></p>
<p>Total Courses: <strong><%= request.getAttribute("courseCount") %></strong></p>
<p>Total Registrations: <strong><%= request.getAttribute("registrationCount") %></strong></p>

<hr/>
<p><a href="<%=request.getContextPath()%>/students">Manage Students</a></p>
<p><a href="<%=request.getContextPath()%>/courses">Manage Courses</a></p>
<p><a href="<%=request.getContextPath()%>/registrations">Manage Registrations</a></p>
<p><a href="<%=request.getContextPath()%>/logout">Logout</a></p>
</body>
</html>
