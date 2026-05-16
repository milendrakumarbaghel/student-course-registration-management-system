package com.studentcourse.controller;

import com.studentcourse.dao.StudentDao;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/student/delete")
public class DeleteStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            StudentDao dao = new StudentDao();
            int id = parseInt(req.getParameter("id"));
            if (id <= 0) {
                req.setAttribute("error", "Invalid student ID.");
                req.setAttribute("students", dao.getAllStudents());
                req.getRequestDispatcher("/WEB-INF/views/student-list.jsp").forward(req, resp);
                return;
            }

            if (dao.hasAnyRegistration(id)) {
                req.setAttribute("error", "Student cannot be deleted because registration exists.");
                req.setAttribute("students", dao.getAllStudents());
                req.getRequestDispatcher("/WEB-INF/views/student-list.jsp").forward(req, resp);
                return;
            }

            dao.deleteStudent(id);
            resp.sendRedirect(req.getContextPath() + "/students");
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
