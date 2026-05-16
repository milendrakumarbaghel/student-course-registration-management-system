package com.studentcourse.model;

public class Course {
    private int courseId;
    private String courseName;
    private String duration;
    private double fees;
    private String trainerName;

    public int getCourseId() { return courseId; }

    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }

    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDuration() { return duration; }

    public void setDuration(String duration) { this.duration = duration; }

    public double getFees() { return fees; }

    public void setFees(double fees) { this.fees = fees; }

    public String getTrainerName() { return trainerName; }

    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }
}
