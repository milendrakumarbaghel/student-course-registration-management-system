package com.studentcourse.controller;

import com.studentcourse.dao.StudentDao;
import com.studentcourse.model.Student;
import com.studentcourse.validation.StudentValidator;
import com.studentcourse.util.AuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/_legacy/student-servlet")
public class StudentServlet extends HttpServlet {

    @Override
    public void init() {
        System.out.println("[Lifecycle] StudentServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }

        String path = req.getServletPath();
        StudentDao studentDao = new StudentDao();

        switch (path) {
            case "/students":
                req.setAttribute("students", studentDao.getAllStudents());
                req.getRequestDispatcher("/WEB-INF/views/student-list.jsp").forward(req, resp);
                break;
            case "/student/add":
                req.setAttribute("mode", "add");
                req.getRequestDispatcher("/WEB-INF/views/student-form.jsp").forward(req, resp);
                break;
            case "/student/edit":
                int editId = parseId(req.getParameter("id"));
                Student existing = studentDao.getStudentById(editId);
                if (existing == null) {
                    resp.sendRedirect(req.getContextPath() + "/students");
                    return;
                }
                req.setAttribute("mode", "edit");
                req.setAttribute("student", existing);
                req.getRequestDispatcher("/WEB-INF/views/student-form.jsp").forward(req, resp);
                break;
            case "/student/delete":
                int deleteId = parseId(req.getParameter("id"));
                if (deleteId <= 0) {
                    resp.sendRedirect(req.getContextPath() + "/students");
                    return;
                }
                if (studentDao.hasAnyRegistration(deleteId)) {
                    req.setAttribute("error", "Student cannot be deleted because registration exists.");
                    req.setAttribute("students", studentDao.getAllStudents());
                    req.getRequestDispatcher("/WEB-INF/views/student-list.jsp").forward(req, resp);
                    return;
                }
                studentDao.deleteStudent(deleteId);
                resp.sendRedirect(req.getContextPath() + "/students");
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/students");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }

        String path = req.getServletPath();
        if (!"/student/add".equals(path) && !"/student/edit".equals(path)) {
            resp.sendRedirect(req.getContextPath() + "/students");
            return;
        }

        Student student = new Student();
        student.setStudentName(safe(req.getParameter("studentName")));
        student.setEmail(safe(req.getParameter("email")));
        student.setPhone(safe(req.getParameter("phone")));
        student.setCity(safe(req.getParameter("city")));

        int age = parseId(req.getParameter("age"));
        boolean isEdit = "/student/edit".equals(path);
        if (isEdit) {
            student.setStudentId(parseId(req.getParameter("studentId")));
        }
        String error = StudentValidator.validate(student, age, isEdit);

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("mode", isEdit ? "edit" : "add");
            student.setAge(age);
            req.setAttribute("student", student);
            req.getRequestDispatcher("/WEB-INF/views/student-form.jsp").forward(req, resp);
            return;
        }

        student.setAge(age);
        StudentDao studentDao = new StudentDao();

        if ("/student/add".equals(path)) {
            studentDao.addStudent(student);
        } else {
            student.setStudentId(parseId(req.getParameter("studentId")));
            studentDao.updateStudent(student);
        }

        resp.sendRedirect(req.getContextPath() + "/students");
    }

    @Override
    public void destroy() {
        System.out.println("[Lifecycle] StudentServlet destroyed");
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
