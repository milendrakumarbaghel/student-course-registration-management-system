package com.studentcourse.controller;

import com.studentcourse.dao.CourseDao;
import com.studentcourse.model.Course;
import com.studentcourse.validation.CourseValidator;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/course/add")
public class AddCourseServlet extends HttpServlet {

    @Override
    public void init() {
        System.out.println("[Lifecycle] AddCourseServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        req.setAttribute("mode", "add");
        req.getRequestDispatcher("/WEB-INF/views/course-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            Course course = new Course();
            course.setCourseName(safe(req.getParameter("courseName")));
            course.setDuration(safe(req.getParameter("duration")));
            course.setTrainerName(safe(req.getParameter("trainerName")));
            double fees = parseDouble(req.getParameter("fees"));

            String error = CourseValidator.validate(course, fees, false);
            if (error != null) {
                course.setFees(fees);
                req.setAttribute("error", error);
                req.setAttribute("mode", "add");
                req.setAttribute("course", course);
                req.getRequestDispatcher("/WEB-INF/views/course-form.jsp").forward(req, resp);
                return;
            }

            course.setFees(fees);
            new CourseDao().addCourse(course);
            resp.sendRedirect(req.getContextPath() + "/courses");
        } catch (RuntimeException ex) {
            ErrorUtil.forwardToErrorPage(req, resp, "Database connection failure. Please try again later.");
        }
    }

    @Override
    public void destroy() {
        System.out.println("[Lifecycle] AddCourseServlet destroyed");
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return -1;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
