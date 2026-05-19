package com.studentcourse.validation;

import com.studentcourse.model.Student;

import java.util.regex.Pattern;

public final class StudentValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,15}$");

    private StudentValidator() {
    }

    public static String validate(Student student, int age, boolean requireId) {
        if (student == null) {
            return "Student is required.";
        }
        if (requireId && student.getStudentId() <= 0) {
            return "Invalid student ID.";
        }
        if (isBlank(student.getStudentName())
                || isBlank(student.getEmail())
                || isBlank(student.getPhone())
                || isBlank(student.getCity())) {
            return "All fields are required.";
        }
        if (!EMAIL_PATTERN.matcher(student.getEmail().trim()).matches()) {
            return "Email format is invalid.";
        }
        if (!PHONE_PATTERN.matcher(student.getPhone().trim()).matches()) {
            return "Phone must be 10 to 15 digits.";
        }
        if (age < 18) {
            return "Age must be 18 or above.";
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
