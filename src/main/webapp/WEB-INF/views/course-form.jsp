<%@ page import="com.studentcourse.model.Course" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String mode = (String) request.getAttribute("mode");
    Course course = (Course) request.getAttribute("course");
    String error = (String) request.getAttribute("error");

    boolean edit = "edit".equals(mode);
    if (course == null) {
        course = new Course();
    }
%>
<html>
<head>
    <title><%= edit ? "Edit Course" : "Add Course" %></title>
</head>
<body>
<h2><%= edit ? "Edit Course" : "Add Course" %></h2>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<form action="<%=request.getContextPath()%>/<%= edit ? "course/update" : "course/add" %>" method="post">
    <% if (edit) { %>
    <input type="hidden" name="courseId" value="<%= course.getCourseId() %>" />
    <% } %>

    <label for="courseName">Course Name:</label>
    <input id="courseName" type="text" name="courseName" value="<%= course.getCourseName() == null ? "" : course.getCourseName() %>" /><br/><br/>

    <label for="duration">Duration:</label>
    <input id="duration" type="text" name="duration" value="<%= course.getDuration() == null ? "" : course.getDuration() %>" /><br/><br/>

    <label for="fees">Fees:</label>
    <input id="fees" type="number" step="0.01" name="fees" value="<%= course.getFees() <= 0 ? "" : course.getFees() %>" /><br/><br/>

    <label for="trainerName">Trainer Name:</label>
    <input id="trainerName" type="text" name="trainerName" value="<%= course.getTrainerName() == null ? "" : course.getTrainerName() %>" /><br/><br/>

    <button type="submit"><%= edit ? "Update" : "Add" %></button>
</form>

<p><a href="<%=request.getContextPath()%>/courses">Back to Courses</a></p>
</body>
</html>

