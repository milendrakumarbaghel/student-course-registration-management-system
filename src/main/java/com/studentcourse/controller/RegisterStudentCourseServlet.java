package com.studentcourse.controller;

import com.studentcourse.dao.CourseDao;
import com.studentcourse.dao.RegistrationDao;
import com.studentcourse.dao.StudentDao;
import com.studentcourse.model.Registration;
import com.studentcourse.validation.RegistrationValidator;
import com.studentcourse.util.AuthUtil;
import com.studentcourse.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/registration/add-action")
public class RegisterStudentCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }
        try {
            int studentId = parseInt(req.getParameter("studentId"));
            int courseId = parseInt(req.getParameter("courseId"));
            String registrationDateText = safe(req.getParameter("registrationDate"));
            String status = safe(req.getParameter("status"));

            RegistrationDao registrationDao = new RegistrationDao();
            String error = RegistrationValidator.validate(studentId, courseId, registrationDateText, status);
            if (error == null && registrationDao.hasActiveDuplicate(studentId, courseId)) {
                error = "Active registration for this student-course pair already exists.";
            }

            if (error != null) {
                req.setAttribute("error", error);
                req.setAttribute("students", new StudentDao().getAllStudents());
                req.setAttribute("courses", new CourseDao().getAllCourses());
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

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
