package com.studentcourse.dao;

import com.studentcourse.model.Student;
import com.studentcourse.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    public boolean addStudent(Student student) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO students(student_name,email,phone,age,city) VALUES(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, student.getStudentName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.setInt(4, student.getAge());
            ps.setString(5, student.getCity());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add student", e);
        }
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM students ORDER BY student_id DESC";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setStudentName(rs.getString("student_name"));
                s.setEmail(rs.getString("email"));
                s.setPhone(rs.getString("phone"));
                s.setAge(rs.getInt("age"));
                s.setCity(rs.getString("city"));
                list.add(s);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch students", e);
        }

        return list;
    }

    public Student getStudentById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM students WHERE student_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setStudentName(rs.getString("student_name"));
                s.setEmail(rs.getString("email"));
                s.setPhone(rs.getString("phone"));
                s.setAge(rs.getInt("age"));
                s.setCity(rs.getString("city"));
                return s;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch student", e);
        }
        return null;
    }

    public boolean updateStudent(Student student) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "UPDATE students SET student_name=?, email=?, phone=?, age=?, city=? WHERE student_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, student.getStudentName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.setInt(4, student.getAge());
            ps.setString(5, student.getCity());
            ps.setInt(6, student.getStudentId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update student", e);
        }
    }

    public boolean hasAnyRegistration(int studentId) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT 1 FROM registrations WHERE student_id=? LIMIT 1";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check student registrations", e);
        }
    }

    public boolean deleteStudent(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "DELETE FROM students WHERE student_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    public int getStudentCount() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM students";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to count students", e);
        }
        return 0;
    }
}
