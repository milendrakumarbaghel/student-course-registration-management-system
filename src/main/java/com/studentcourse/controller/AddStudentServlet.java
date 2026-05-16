package com.studentcourse.controller;

import com.studentcourse.dao.StudentDao;
import com.studentcourse.model.Student;
import com.studentcourse.validation.StudentValidator;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/student/add")
public class AddStudentServlet extends HttpServlet {

    @Override
    public void init() {
        System.out.println("[Lifecycle] AddStudentServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        req.setAttribute("mode", "add");
        req.getRequestDispatcher("/WEB-INF/views/student-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            Student student = new Student();
            student.setStudentName(safe(req.getParameter("studentName")));
            student.setEmail(safe(req.getParameter("email")));
            student.setPhone(safe(req.getParameter("phone")));
            student.setCity(safe(req.getParameter("city")));
            int age = parseInt(req.getParameter("age"));

            String error = StudentValidator.validate(student, age, false);
            if (error != null) {
                student.setAge(age);
                req.setAttribute("error", error);
                req.setAttribute("mode", "add");
                req.setAttribute("student", student);
                req.getRequestDispatcher("/WEB-INF/views/student-form.jsp").forward(req, resp);
                return;
            }

            student.setAge(age);
            new StudentDao().addStudent(student);
            resp.sendRedirect(req.getContextPath() + "/students");
        } catch (RuntimeException ex) {
            ErrorUtil.forwardToErrorPage(req, resp, "Database connection failure. Please try again later.");
        }
    }

    @Override
    public void destroy() {
        System.out.println("[Lifecycle] AddStudentServlet destroyed");
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
