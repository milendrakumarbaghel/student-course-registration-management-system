<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String rememberedUsername = (String) request.getAttribute("rememberedUsername");
    if (rememberedUsername == null) {
        rememberedUsername = "";
    }
    String error = (String) request.getAttribute("error");
%>
<html>
<head>
    <title>Admin Login</title>
</head>
<body>
<h2>Admin Login</h2>
<form action="<%=request.getContextPath()%>/login-action" method="post">
    <label for="username">Username:</label>
    <input id="username" type="text" name="username" value="<%= rememberedUsername %>" /><br/><br/>

    <label for="password">Password:</label>
    <input id="password" type="password" name="password" /><br/><br/>

    <label for="remember">
        <input id="remember" type="checkbox" name="remember" <%= rememberedUsername.isEmpty() ? "" : "checked" %> /> Remember Username
    </label><br/><br/>

    <button type="submit">Login</button>
</form>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<p><a href="<%=request.getContextPath()%>/register-admin">Register Admin</a></p>
</body>
</html>
