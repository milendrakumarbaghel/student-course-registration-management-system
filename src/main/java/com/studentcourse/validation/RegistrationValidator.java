package com.studentcourse.validation;

import java.sql.Date;

public final class RegistrationValidator {
    private RegistrationValidator() {
    }

    public static String validate(int studentId, int courseId, String registrationDateText, String status) {
        if (studentId <= 0 || courseId <= 0 || isBlank(registrationDateText) || isBlank(status)) {
            return "All fields are required.";
        }
        if (!isValidStatus(status)) {
            return "Status must be Active, Completed, or Cancelled.";
        }
        try {
            Date registrationDate = Date.valueOf(registrationDateText);
            Date today = new Date(System.currentTimeMillis());
            if (registrationDate.after(today)) {
                return "Registration date cannot be after today.";
            }
        } catch (Exception e) {
            return "Registration date is invalid.";
        }
        return null;
    }

    public static boolean isValidStatus(String status) {
        return "Active".equals(status) || "Completed".equals(status) || "Cancelled".equals(status);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
