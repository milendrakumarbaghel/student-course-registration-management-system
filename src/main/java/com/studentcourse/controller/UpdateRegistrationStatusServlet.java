package com.studentcourse.controller;

import com.studentcourse.dao.RegistrationDao;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registration/status")
public class UpdateRegistrationStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        update(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        update(req, resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }

        int id = parseInt(req.getParameter("id"));
        String status = safe(req.getParameter("status"));
        try {
            if (id <= 0 || !isValidStatus(status)) {
                req.setAttribute("error", "Invalid registration ID or status.");
                req.setAttribute("registrations", new RegistrationDao().getAllRegistrations());
                req.getRequestDispatcher("/WEB-INF/views/registration-list.jsp").forward(req, resp);
                return;
            }

            new RegistrationDao().updateRegistrationStatus(id, status);
        } catch (RuntimeException ex) {
            ErrorUtil.forwardToErrorPage(req, resp, "Database connection failure. Please try again later.");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/registrations");
    }

    private boolean isValidStatus(String status) {
        return "Active".equals(status) || "Completed".equals(status) || "Cancelled".equals(status);
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
