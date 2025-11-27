/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

public class Enrollment {
    // --- Attributes (Encapsulation) ---
    private final String courseId;
    private final String courseTitle;
    private final int creditHours;
    private final String finalGrade;
    private final double gradePoint;
    
    // The Enrollment object is created by consolidating data from the Course object
    // and the StudentMark.txt file.
    public Enrollment(Course course, String finalGrade, double gradePoint) {
        this.courseId = course.getCourseId();
        this.courseTitle = course.getCourseName(); // Assuming Course has getCourseName()
        this.creditHours = course.getCredits();     // Assuming Course has getCredits()
        this.finalGrade = finalGrade;
        this.gradePoint = gradePoint;
    }

    // --- Getters (Required by JTable and ReportGenerator) ---

    // Fixes the error on line 79/80 (if title/ID were missing)
    public String getCourseId() { return courseId; } 
    public String getCourseTitle() { return courseTitle; } 

    // Fixes the error on line 81
    public int getCreditHours() { return creditHours; }   

    // Required for displaying the Grade
    public String getFinalGrade() { return finalGrade; }

    // Required for CGPA calculation and GP column in report
    public double getGradePoint() { return gradePoint; }
}
