package com.studentcourse.controller;

import com.studentcourse.dao.CourseDao;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/course/delete")
public class DeleteCourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            CourseDao dao = new CourseDao();
            int id = parseInt(req.getParameter("id"));
            if (id <= 0) {
                req.setAttribute("error", "Invalid course ID.");
                req.setAttribute("courses", dao.getAllCourses());
                req.getRequestDispatcher("/WEB-INF/views/course-list.jsp").forward(req, resp);
                return;
            }

            if (dao.hasActiveRegistration(id)) {
                req.setAttribute("error", "Course cannot be deleted because active registration exists.");
                req.setAttribute("courses", dao.getAllCourses());
                req.getRequestDispatcher("/WEB-INF/views/course-list.jsp").forward(req, resp);
                return;
            }

            dao.deleteCourse(id);
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
}
