package com.studentcourse.controller;

import com.studentcourse.util.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/check")
public class ConnectionCheck extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DBConnection.checkUser();
        resp.setContentType("text/plain");
        resp.getWriter().write("Database connection check executed. See server logs.");
    }
}
