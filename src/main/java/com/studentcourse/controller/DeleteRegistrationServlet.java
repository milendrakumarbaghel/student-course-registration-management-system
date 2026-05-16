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

@WebServlet("/registration/delete")
public class DeleteRegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            int id = parseInt(req.getParameter("id"));
            if (id <= 0) {
                req.setAttribute("error", "Invalid registration ID.");
                req.setAttribute("registrations", new RegistrationDao().getAllRegistrations());
                req.getRequestDispatcher("/WEB-INF/views/registration-list.jsp").forward(req, resp);
                return;
            }
            new RegistrationDao().deleteRegistration(id);
            resp.sendRedirect(req.getContextPath() + "/registrations");
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
