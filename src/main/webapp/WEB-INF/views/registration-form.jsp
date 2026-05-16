<%@ page import="java.util.List" %>
<%@ page import="com.studentcourse.model.Student" %>
<%@ page import="com.studentcourse.model.Course" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    List<?> students = (List<?>) request.getAttribute("students");
    List<?> courses = (List<?>) request.getAttribute("courses");
    String error = (String) request.getAttribute("error");

    int selectedStudentId = request.getAttribute("selectedStudentId") == null ? -1 : (Integer) request.getAttribute("selectedStudentId");
    int selectedCourseId = request.getAttribute("selectedCourseId") == null ? -1 : (Integer) request.getAttribute("selectedCourseId");
    String selectedDate = request.getAttribute("selectedDate") == null ? "" : (String) request.getAttribute("selectedDate");
    String selectedStatus = request.getAttribute("selectedStatus") == null ? "Active" : (String) request.getAttribute("selectedStatus");
%>
<html>
<head>
    <title>Add Registration</title>
</head>
<body>
<h2>Add Student-Course Registration</h2>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<form action="<%=request.getContextPath()%>/registration/add-action" method="post">
    <label for="studentId">Student:</label>
    <select id="studentId" name="studentId">
        <option value="">Select student</option>
        <% if (students != null) {
            for (Object obj : students) {
                Student s = (Student) obj; %>
        <option value="<%= s.getStudentId() %>" <%= selectedStudentId == s.getStudentId() ? "selected" : "" %>><%= s.getStudentName() %></option>
        <%  }
        } %>
    </select><br/><br/>

    <label for="courseId">Course:</label>
    <select id="courseId" name="courseId">
        <option value="">Select course</option>
        <% if (courses != null) {
            for (Object obj : courses) {
                Course c = (Course) obj; %>
        <option value="<%= c.getCourseId() %>" <%= selectedCourseId == c.getCourseId() ? "selected" : "" %>><%= c.getCourseName() %></option>
        <%  }
        } %>
    </select><br/><br/>

    <label for="registrationDate">Registration Date:</label>
    <input id="registrationDate" type="date" name="registrationDate" value="<%= selectedDate %>" /><br/><br/>

    <label for="status">Status:</label>
    <select id="status" name="status">
        <option value="Active" <%= "Active".equals(selectedStatus) ? "selected" : "" %>>Active</option>
        <option value="Completed" <%= "Completed".equals(selectedStatus) ? "selected" : "" %>>Completed</option>
        <option value="Cancelled" <%= "Cancelled".equals(selectedStatus) ? "selected" : "" %>>Cancelled</option>
    </select><br/><br/>

    <button type="submit">Save Registration</button>
</form>

<p><a href="<%=request.getContextPath()%>/registrations">Back to Registrations</a></p>
</body>
</html>
