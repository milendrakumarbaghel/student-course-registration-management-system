package com.studentcourse.dao;

import com.studentcourse.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDao {

    public boolean registerAdmin(String username, String password) {
        boolean isInserted = false;

        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO admin(username, password) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                isInserted = true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to register admin", e);
        }
        return isInserted;
    }

    public boolean validateAdmin(String username, String password) {
        boolean isValid = false;

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT 1 FROM admin WHERE username=? AND password=?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isValid = true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate admin", e);
        }

        return isValid;
    }

    public boolean isUsernameTaken(String username) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT 1 FROM admin WHERE username=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check admin username", e);
        }
    }

    public void ensureDefaultAdminExists() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM admin WHERE username='admin'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                registerAdmin("admin", "admin123");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to seed default admin", e);
        }
    }
}
