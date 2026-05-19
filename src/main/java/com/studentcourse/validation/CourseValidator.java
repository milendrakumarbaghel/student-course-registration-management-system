package com.studentcourse.validation;

import com.studentcourse.model.Course;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CourseValidator {
    private static final Pattern DURATION_PATTERN = Pattern.compile("^(\\d+)\\s*(month|months)?$", Pattern.CASE_INSENSITIVE);

    private CourseValidator() {
    }

    public static String validate(Course course, double fees, boolean requireId) {
        if (course == null) {
            return "Course is required.";
        }
        if (requireId && course.getCourseId() <= 0) {
            return "Invalid course ID.";
        }
        if (isBlank(course.getCourseName())
                || isBlank(course.getDuration())
                || isBlank(course.getTrainerName())) {
            return "All fields are required.";
        }
        if (!isValidDuration(course.getDuration())) {
            return "Duration must be a positive number of months (e.g., 6 or 6 months).";
        }
        if (fees <= 0) {
            return "Fees must be greater than 0.";
        }
        return null;
    }

    private static boolean isValidDuration(String duration) {
        if (duration == null) {
            return false;
        }
        Matcher matcher = DURATION_PATTERN.matcher(duration.trim());
        if (!matcher.matches()) {
            return false;
        }
        try {
            int months = Integer.parseInt(matcher.group(1));
            return months > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
