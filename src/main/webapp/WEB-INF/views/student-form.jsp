<%@ page import="com.studentcourse.model.Student" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    Student student = (Student) request.getAttribute("student");
    String error = (String) request.getAttribute("error");

    boolean edit = "edit".equals(mode);
    if (student == null) {
        student = new Student();
    }
%>
<html>
<head>
    <title><%= edit ? "Edit Student" : "Add Student" %></title>
</head>
<body>
<h2><%= edit ? "Edit Student" : "Add Student" %></h2>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<form action="<%=request.getContextPath()%>/<%= edit ? "student/update" : "student/add" %>" method="post">
    <% if (edit) { %>
    <input type="hidden" name="studentId" value="<%= student.getStudentId() %>" />
    <% } %>

    <label for="studentName">Name:</label>
    <input id="studentName" type="text" name="studentName" value="<%= student.getStudentName() == null ? "" : student.getStudentName() %>" /><br/><br/>

    <label for="email">Email:</label>
    <input id="email" type="text" name="email" value="<%= student.getEmail() == null ? "" : student.getEmail() %>" /><br/><br/>

    <label for="phone">Phone:</label>
    <input id="phone" type="text" name="phone" value="<%= student.getPhone() == null ? "" : student.getPhone() %>" /><br/><br/>

    <label for="age">Age:</label>
    <input id="age" type="number" name="age" value="<%= student.getAge() <= 0 ? "" : student.getAge() %>" /><br/><br/>

    <label for="city">City:</label>
    <input id="city" type="text" name="city" value="<%= student.getCity() == null ? "" : student.getCity() %>" /><br/><br/>

    <button type="submit"><%= edit ? "Update" : "Add" %></button>
</form>

<p><a href="<%=request.getContextPath()%>/students">Back to Students</a></p>
</body>
</html>
