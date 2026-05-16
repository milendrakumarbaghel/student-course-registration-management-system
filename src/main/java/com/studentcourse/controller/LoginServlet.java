package com.studentcourse.controller;

import com.studentcourse.dao.AdminDao;
import com.studentcourse.validation.LoginValidator;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login-action")
public class LoginServlet extends HttpServlet {

    @Override
    public void init() {
        System.out.println("[Lifecycle] LoginServlet initialized");
        try {
            new AdminDao().ensureDefaultAdminExists();
        } catch (RuntimeException ex) {
            System.out.println("[Lifecycle] LoginServlet init warning: " + ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = safe(req.getParameter("username"));
            String password = safe(req.getParameter("password"));
            String remember = req.getParameter("remember");

            String error = LoginValidator.validate(username, password);
            if (error != null) {
                req.setAttribute("error", error);
                req.setAttribute("rememberedUsername", username);
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            AdminDao adminDao = new AdminDao();
            boolean isValid = adminDao.validateAdmin(username, password);

            if (isValid) {
                HttpSession session = req.getSession(true);
                session.setAttribute("loggedInUser", username);
                session.setAttribute("loginTime", System.currentTimeMillis());

                Cookie cookie = new Cookie("rememberedUsername", "on".equals(remember) ? username : "");
                cookie.setMaxAge("on".equals(remember) ? (60 * 60 * 24 * 7) : 0);
                cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                resp.addCookie(cookie);

                resp.sendRedirect(req.getContextPath() + "/dashboard");
            } else {
                req.setAttribute("error", "Invalid username or password.");
                req.setAttribute("rememberedUsername", username);
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }
        } catch (RuntimeException ex) {
            ErrorUtil.forwardToErrorPage(req, resp, "Database connection failure. Please try again later.");
        }
    }

    @Override
    public void destroy() {
        System.out.println("[Lifecycle] LoginServlet destroyed");
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
