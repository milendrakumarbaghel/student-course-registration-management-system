package com.studentcourse.model;

import java.util.Date;

public class Registration {
    private int registrationId;
    private int studentId;
    private int courseId;
    private Date registrationDate;
    private String status;
    private String studentName;
    private String courseName;

    public int getRegistrationId() { return registrationId; }

    public void setRegistrationId(int registrationId) { this.registrationId = registrationId; }

    public int getStudentId() { return studentId; }

    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }

    public void setCourseId(int courseId) { this.courseId = courseId; }

    public Date getRegistrationDate() { return registrationDate; }

    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getStudentName() { return studentName; }

    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCourseName() { return courseName; }

    public void setCourseName(String courseName) { this.courseName = courseName; }
}
