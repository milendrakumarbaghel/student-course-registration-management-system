package com.studentcourse.controller;

import com.studentcourse.dao.CourseDao;
import com.studentcourse.model.Course;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/course/edit")
public class EditCourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            int id = parseInt(req.getParameter("id"));
            if (id <= 0) {
                req.setAttribute("error", "Invalid course ID.");
                req.setAttribute("courses", new CourseDao().getAllCourses());
                req.getRequestDispatcher("/WEB-INF/views/course-list.jsp").forward(req, resp);
                return;
            }

            Course course = new CourseDao().getCourseById(id);
            if (course == null) {
                req.setAttribute("error", "Course record not found.");
                req.setAttribute("courses", new CourseDao().getAllCourses());
                req.getRequestDispatcher("/WEB-INF/views/course-list.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("mode", "edit");
            req.setAttribute("course", course);
            req.getRequestDispatcher("/WEB-INF/views/course-form.jsp").forward(req, resp);
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
}
