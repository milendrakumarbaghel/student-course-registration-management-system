package com.studentcourse.validation;

import com.studentcourse.model.Student;

public final class StudentValidator {
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
        if (age < 18) {
            return "Age must be 18 or above.";
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

