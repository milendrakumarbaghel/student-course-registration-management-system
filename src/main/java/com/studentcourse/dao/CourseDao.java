package com.studentcourse.dao;

import com.studentcourse.model.Course;
import com.studentcourse.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {
    public boolean addCourse(Course course) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO courses(course_name,duration,fees,trainer_name) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDuration());
            ps.setDouble(3, course.getFees());
            ps.setString(4, course.getTrainerName());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Failed to add course", e);
        }
    }

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM courses ORDER BY course_id DESC";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseName(rs.getString("course_name"));
                c.setDuration(rs.getString("duration"));
                c.setFees(rs.getDouble("fees"));
                c.setTrainerName(rs.getString("trainer_name"));
                list.add(c);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch courses", e);
        }
        return list;
    }

    public Course getCourseById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM courses WHERE course_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseName(rs.getString("course_name"));
                c.setDuration(rs.getString("duration"));
                c.setFees(rs.getDouble("fees"));
                c.setTrainerName(rs.getString("trainer_name"));
                return c;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch course", e);
        }
        return null;
    }

    public boolean updateCourse(Course course) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "UPDATE courses SET course_name=?, duration=?, fees=?, trainer_name=? WHERE course_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDuration());
            ps.setDouble(3, course.getFees());
            ps.setString(4, course.getTrainerName());
            ps.setInt(5, course.getCourseId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update course", e);
        }
    }

    public boolean existsById(int courseId) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT 1 FROM courses WHERE course_id=? LIMIT 1";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate course existence", e);
        }
    }

    public boolean hasActiveRegistration(int courseId) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT 1 FROM registrations WHERE course_id=? AND status='Active' LIMIT 1";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check course registrations", e);
        }
    }

    public boolean deleteCourse(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "DELETE FROM courses WHERE course_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete course", e);
        }
    }

    public int getCourseCount() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM courses";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to count courses", e);
        }
        return 0;
    }
}
