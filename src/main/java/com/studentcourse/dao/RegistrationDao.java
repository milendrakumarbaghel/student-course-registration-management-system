package com.studentcourse.dao;

import com.studentcourse.model.Registration;
import com.studentcourse.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDao {
    public boolean addRegistration(Registration registration) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO registrations(student_id,course_id,registration_date,status) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, registration.getStudentId());
            ps.setInt(2, registration.getCourseId());
            ps.setDate(3, new java.sql.Date(registration.getRegistrationDate().getTime()));
            ps.setString(4, registration.getStatus());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Failed to add registration", e);
        }
    }

    public boolean hasActiveDuplicate(int studentId, int courseId) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT 1 FROM registrations WHERE student_id=? AND course_id=? AND status='Active' LIMIT 1";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate duplicate registration", e);
        }
    }

    public List<Registration> getAllRegistrations() {
        List<Registration> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT r.registration_id, r.student_id, r.course_id, r.registration_date, r.status, " +
                    "s.student_name, c.course_name " +
                    "FROM registrations r " +
                    "JOIN students s ON r.student_id=s.student_id " +
                    "JOIN courses c ON r.course_id=c.course_id " +
                    "ORDER BY r.registration_id DESC";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Registration r = new Registration();
                r.setRegistrationId(rs.getInt("registration_id"));
                r.setStudentId(rs.getInt("student_id"));
                r.setCourseId(rs.getInt("course_id"));
                r.setRegistrationDate(rs.getDate("registration_date"));
                r.setStatus(rs.getString("status"));
                r.setStudentName(rs.getString("student_name"));
                r.setCourseName(rs.getString("course_name"));
                list.add(r);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch registrations", e);
        }

        return list;
    }

    public boolean updateRegistrationStatus(int registrationId, String status) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "UPDATE registrations SET status=? WHERE registration_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, registrationId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update registration status", e);
        }
    }

    public boolean deleteRegistration(int registrationId) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "DELETE FROM registrations WHERE registration_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, registrationId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete registration", e);
        }
    }

    public int getRegistrationCount() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM registrations";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to count registrations", e);
        }
        return 0;
    }
}
