package com.studentcourse.validation;

import com.studentcourse.model.Course;

public final class CourseValidator {
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
        if (fees <= 0) {
            return "Fees must be greater than 0.";
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

