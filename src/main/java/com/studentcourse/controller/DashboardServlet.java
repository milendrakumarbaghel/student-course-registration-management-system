package com.studentcourse.controller;

import com.studentcourse.dao.CourseDao;
import com.studentcourse.dao.RegistrationDao;
import com.studentcourse.dao.StudentDao;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    public void init() {
        System.out.println("[Lifecycle] DashboardServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = AuthUtil.requireLoggedInUser(req, resp);
        if (user == null) {
            return;
        }
        try {
            StudentDao studentDao = new StudentDao();
            CourseDao courseDao = new CourseDao();
            RegistrationDao registrationDao = new RegistrationDao();

            req.setAttribute("loggedInUser", user);
            req.setAttribute("studentCount", studentDao.getStudentCount());
            req.setAttribute("courseCount", courseDao.getCourseCount());
            req.setAttribute("registrationCount", registrationDao.getRegistrationCount());
            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
        } catch (RuntimeException ex) {
            ErrorUtil.forwardToErrorPage(req, resp, "Database connection failure. Please try again later.");
        }
    }

    @Override
    public void destroy() {
        System.out.println("[Lifecycle] DashboardServlet destroyed");
    }
}
