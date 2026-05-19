package com.studentcourse.controller;

import com.studentcourse.dao.CourseDao;
import com.studentcourse.dao.RegistrationDao;
import com.studentcourse.dao.StudentDao;
import com.studentcourse.model.Registration;
import com.studentcourse.validation.RegistrationValidator;
import com.studentcourse.util.AuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/_legacy/registration-servlet")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }

        String path = req.getServletPath();
        RegistrationDao registrationDao = new RegistrationDao();

        switch (path) {
            case "/registrations":
                req.setAttribute("registrations", registrationDao.getAllRegistrations());
                req.getRequestDispatcher("/WEB-INF/views/registration-list.jsp").forward(req, resp);
                break;
            case "/registration/add":
                req.setAttribute("students", new StudentDao().getAllStudents());
                req.setAttribute("courses", new CourseDao().getAllCourses());
                req.getRequestDispatcher("/WEB-INF/views/registration-form.jsp").forward(req, resp);
                break;
            case "/registration/delete":
                int id = parseId(req.getParameter("id"));
                if (id > 0) {
                    registrationDao.deleteRegistration(id);
                }
                resp.sendRedirect(req.getContextPath() + "/registrations");
                break;
            case "/registration/status":
                int registrationId = parseId(req.getParameter("id"));
                String status = safe(req.getParameter("status"));
                if (registrationId > 0 && RegistrationValidator.isValidStatus(status)) {
                    registrationDao.updateRegistrationStatus(registrationId, status);
                }
                resp.sendRedirect(req.getContextPath() + "/registrations");
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/registrations");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }

        if (!"/registration/add".equals(req.getServletPath())) {
            resp.sendRedirect(req.getContextPath() + "/registrations");
            return;
        }

        int studentId = parseId(req.getParameter("studentId"));
        int courseId = parseId(req.getParameter("courseId"));
        String registrationDateText = safe(req.getParameter("registrationDate"));
        String status = safe(req.getParameter("status"));

        String error = RegistrationValidator.validate(studentId, courseId, registrationDateText, status);
        RegistrationDao registrationDao = new RegistrationDao();
        StudentDao studentDao = new StudentDao();
        CourseDao courseDao = new CourseDao();

        if (error == null && !studentDao.existsById(studentId)) {
            error = "Invalid student ID.";
        }
        if (error == null && !courseDao.existsById(courseId)) {
            error = "Invalid course ID.";
        }
        if (error == null && registrationDao.hasActiveDuplicate(studentId, courseId)) {
            error = "Active registration for this student-course pair already exists.";
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("students", studentDao.getAllStudents());
            req.setAttribute("courses", courseDao.getAllCourses());
            req.setAttribute("selectedStudentId", studentId);
            req.setAttribute("selectedCourseId", courseId);
            req.setAttribute("selectedDate", registrationDateText);
            req.setAttribute("selectedStatus", status);
            req.getRequestDispatcher("/WEB-INF/views/registration-form.jsp").forward(req, resp);
            return;
        }

        Registration registration = new Registration();
        registration.setStudentId(studentId);
        registration.setCourseId(courseId);
        registration.setRegistrationDate(Date.valueOf(registrationDateText));
        registration.setStatus(status);

        registrationDao.addRegistration(registration);
        resp.sendRedirect(req.getContextPath() + "/registrations");
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private int parseId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }
}
