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

@WebServlet("/course/update")
public class UpdateCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            Course course = new Course();
            course.setCourseId(parseInt(req.getParameter("courseId")));
            course.setCourseName(safe(req.getParameter("courseName")));
            course.setDuration(safe(req.getParameter("duration")));
            course.setTrainerName(safe(req.getParameter("trainerName")));
            double fees = parseDouble(req.getParameter("fees"));

            String error = CourseValidator.validate(course, fees, true);
            if (error != null) {
                course.setFees(fees);
                req.setAttribute("error", error);
                req.setAttribute("mode", "edit");
                req.setAttribute("course", course);
                req.getRequestDispatcher("/WEB-INF/views/course-form.jsp").forward(req, resp);
                return;
            }

            course.setFees(fees);
            new CourseDao().updateCourse(course);
            resp.sendRedirect(req.getContextPath() + "/courses");
        } catch (RuntimeException ex) {
            ErrorUtil.forwardToErrorPage(req, resp, "Database connection failure. Please try again later.");
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
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
