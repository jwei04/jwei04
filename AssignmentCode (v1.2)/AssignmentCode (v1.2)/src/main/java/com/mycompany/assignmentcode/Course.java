/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author JIN WEI
 */
public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    // We omit the remaining fields from the file (Instructor, Weights) 
    // as they aren't directly needed for CGPA calculation or the core report.

    public Course(String courseId, String courseName, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
    }

    // Getters (Encapsulation)
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
}

