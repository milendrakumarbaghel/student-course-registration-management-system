package com.studentcourse.controller;

import com.studentcourse.dao.CourseDao;
import com.studentcourse.model.Course;
import com.studentcourse.validation.CourseValidator;
import com.studentcourse.util.AuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/_legacy/course-servlet")
public class CourseServlet extends HttpServlet {

    @Override
    public void init() {
        System.out.println("[Lifecycle] CourseServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }

        String path = req.getServletPath();
        CourseDao courseDao = new CourseDao();

        switch (path) {
            case "/courses":
                req.setAttribute("courses", courseDao.getAllCourses());
                req.getRequestDispatcher("/WEB-INF/views/course-list.jsp").forward(req, resp);
                break;
            case "/course/add":
                req.setAttribute("mode", "add");
                req.getRequestDispatcher("/WEB-INF/views/course-form.jsp").forward(req, resp);
                break;
            case "/course/edit":
                int editId = parseId(req.getParameter("id"));
                Course existing = courseDao.getCourseById(editId);
                if (existing == null) {
                    resp.sendRedirect(req.getContextPath() + "/courses");
                    return;
                }
                req.setAttribute("mode", "edit");
                req.setAttribute("course", existing);
                req.getRequestDispatcher("/WEB-INF/views/course-form.jsp").forward(req, resp);
                break;
            case "/course/delete":
                int deleteId = parseId(req.getParameter("id"));
                if (deleteId <= 0) {
                    resp.sendRedirect(req.getContextPath() + "/courses");
                    return;
                }
                if (courseDao.hasActiveRegistration(deleteId)) {
                    req.setAttribute("error", "Course cannot be deleted because active registration exists.");
                    req.setAttribute("courses", courseDao.getAllCourses());
                    req.getRequestDispatcher("/WEB-INF/views/course-list.jsp").forward(req, resp);
                    return;
                }
                courseDao.deleteCourse(deleteId);
                resp.sendRedirect(req.getContextPath() + "/courses");
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/courses");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AuthUtil.requireLoggedInUser(req, resp) == null) {
            return;
        }

        String path = req.getServletPath();
        if (!"/course/add".equals(path) && !"/course/edit".equals(path)) {
            resp.sendRedirect(req.getContextPath() + "/courses");
            return;
        }

        Course course = new Course();
        course.setCourseName(safe(req.getParameter("courseName")));
        course.setDuration(safe(req.getParameter("duration")));
        course.setTrainerName(safe(req.getParameter("trainerName")));

        double fees = parseDouble(req.getParameter("fees"));
        boolean isEdit = "/course/edit".equals(path);
        if (isEdit) {
            course.setCourseId(parseId(req.getParameter("courseId")));
        }
        String error = CourseValidator.validate(course, fees, isEdit);

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("mode", isEdit ? "edit" : "add");
            course.setFees(fees);
            req.setAttribute("course", course);
            req.getRequestDispatcher("/WEB-INF/views/course-form.jsp").forward(req, resp);
            return;
        }

        course.setFees(fees);
        CourseDao courseDao = new CourseDao();

        if ("/course/add".equals(path)) {
            courseDao.addCourse(course);
        } else {
            course.setCourseId(parseId(req.getParameter("courseId")));
            courseDao.updateCourse(course);
        }

        resp.sendRedirect(req.getContextPath() + "/courses");
    }

    @Override
    public void destroy() {
        System.out.println("[Lifecycle] CourseServlet destroyed");
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

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return -1;
        }
    }
}
