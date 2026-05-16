<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
%>
<html>
<head>
    <title>Register Admin</title>
</head>
<body>
<h2>Register Admin</h2>
<form action="<%=request.getContextPath()%>/register-admin" method="post">
    <label>Username:</label>
    <input type="text" name="username" /><br/><br/>

    <label>Password:</label>
    <input type="password" name="password" /><br/><br/>

    <button type="submit">Register</button>
</form>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<p><a href="<%=request.getContextPath()%>/login">Back to Login</a></p>
</body>
</html>

