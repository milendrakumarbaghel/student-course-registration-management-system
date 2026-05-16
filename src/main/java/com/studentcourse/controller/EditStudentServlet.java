package com.studentcourse.controller;

import com.studentcourse.dao.StudentDao;
import com.studentcourse.model.Student;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/student/edit")
public class EditStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            int id = parseInt(req.getParameter("id"));
            if (id <= 0) {
                req.setAttribute("error", "Invalid student ID.");
                req.setAttribute("students", new StudentDao().getAllStudents());
                req.getRequestDispatcher("/WEB-INF/views/student-list.jsp").forward(req, resp);
                return;
            }

            Student student = new StudentDao().getStudentById(id);
            if (student == null) {
                req.setAttribute("error", "Student record not found.");
                req.setAttribute("students", new StudentDao().getAllStudents());
                req.getRequestDispatcher("/WEB-INF/views/student-list.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("mode", "edit");
            req.setAttribute("student", student);
            req.getRequestDispatcher("/WEB-INF/views/student-form.jsp").forward(req, resp);
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
