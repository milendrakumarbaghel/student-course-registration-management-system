package com.studentcourse.controller;

import com.studentcourse.dao.CourseDao;
import com.studentcourse.dao.StudentDao;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registration/add")
public class RegistrationFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            req.setAttribute("students", new StudentDao().getAllStudents());
            req.setAttribute("courses", new CourseDao().getAllCourses());
            req.getRequestDispatcher("/WEB-INF/views/registration-form.jsp").forward(req, resp);
        } catch (RuntimeException ex) {
            ErrorUtil.forwardToErrorPage(req, resp, "Database connection failure. Please try again later.");
        }
    }
}
