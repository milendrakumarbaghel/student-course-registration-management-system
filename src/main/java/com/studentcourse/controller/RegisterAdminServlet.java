package com.studentcourse.controller;

import com.studentcourse.dao.AdminDao;
import com.studentcourse.validation.AdminValidator;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register-admin")
public class RegisterAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register_admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = safe(req.getParameter("username"));
            String password = safe(req.getParameter("password"));

            String error = AdminValidator.validate(username, password);
            if (error != null) {
                req.setAttribute("error", error);
                req.getRequestDispatcher("/WEB-INF/views/register_admin.jsp").forward(req, resp);
                return;
            }

            AdminDao adminDao = new AdminDao();
            if (adminDao.isUsernameTaken(username)) {
                req.setAttribute("error", "Username already exists.");
                req.getRequestDispatcher("/WEB-INF/views/register_admin.jsp").forward(req, resp);
                return;
            }

            if (adminDao.registerAdmin(username, password)) {
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                req.setAttribute("error", "Unable to register admin. Try again.");
                req.getRequestDispatcher("/WEB-INF/views/register_admin.jsp").forward(req, resp);
            }
        } catch (RuntimeException ex) {
            ErrorUtil.forwardToErrorPage(req, resp, "Database connection failure. Please try again later.");
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
