package com.studentcourse.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:mysql://localhost:3306/student_course_registration"
    );
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASS = System.getenv().getOrDefault("DB_PASS", "admin");

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void checkUser() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection ignored = DriverManager.getConnection(URL, USER, PASS)) {
                System.out.println("Connected to database");
            }
        } catch (Exception e) {
            throw new RuntimeException("Database check failed", e);
        }
    }
}