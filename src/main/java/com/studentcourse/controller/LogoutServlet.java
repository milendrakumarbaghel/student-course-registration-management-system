package com.studentcourse.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        clearCookie(req, resp, "rememberedUsername");
        clearCookie(req, resp, "JSESSIONID");

        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private void clearCookie(HttpServletRequest req, HttpServletResponse resp, String name) {
        String contextPath = req.getContextPath();
        String cookiePath = contextPath == null || contextPath.isEmpty() ? "/" : contextPath;

        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath(cookiePath);
        resp.addCookie(cookie);

        if (!"/".equals(cookiePath)) {
            Cookie rootCookie = new Cookie(name, "");
            rootCookie.setMaxAge(0);
            rootCookie.setPath("/");
            resp.addCookie(rootCookie);
        }
    }
}
