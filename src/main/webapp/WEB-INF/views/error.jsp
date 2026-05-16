<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String message = (String) request.getAttribute("message");
    if (message == null) {
        message = "Something went wrong.";
    }
%>
<html>
<head>
    <title>Error</title>
</head>
<body>
<h2>Error</h2>
<p style="color:red;"><%= message %></p>
<p><a href="<%=request.getContextPath()%>/dashboard">Back to Dashboard</a></p>
</body>
</html>

