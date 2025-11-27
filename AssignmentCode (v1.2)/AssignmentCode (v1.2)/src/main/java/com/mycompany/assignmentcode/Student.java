/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

import java.util.ArrayList;
import java.util.List;

/**
 * The core data model class for a student, handling personal information, 
 * course enrollments, CGPA calculation, and progression eligibility checks.
 */
public class Student {
    
    // --- Attributes (Encapsulation) ---
    private String studentID;
    private String firstName;
    private String lastName;
    private String major; // Used for Program/Major
    private String year;
    private String email;
    
    // List to store all courses taken by the student
    private final List<Enrollment> enrollments; 
    
    // Calculated Attributes 
    private double cgpa;
    private int totalFailedCourses;

    // Constructor (Receiving data directly from AcademicDataManager)
    public Student(String studentID, String firstName, String lastName, String major, String year, String email) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.year = year;
        this.email = email;
        this.enrollments = new ArrayList<>();
        // Initialize volatile attributes
        this.cgpa = 0.0;
        this.totalFailedCourses = 0;
    }

    // --- Data Consolidation Method (Called by the Manager) ---
    /**
     * Adds a course enrollment record to the student's history.
     * @param enrollment The Enrollment object containing course and mark details.
     */
    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }
    
    // --- Getters for Report Display ---
    public String getStudentID() { return studentID; }
    public String getFirstName() { return firstName; } // From first version
    public String getLastName() { return lastName; }   // From first version
    public String getName() { return firstName + " " + lastName; } // Full name for report
    public String getMajor() { return major; }
    public String getYear() { return year; }
    public String getEmail() { return email; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    
    /**
     * Calculates and returns the student's Cumulative GPA (CGPA), rounded to two decimal places.
     * @return The calculated CGPA.
     */
    public double getCgpa() { 
        calculateCGPA(); // Ensure CGPA is up-to-date before returning
        // Return CGPA rounded to two decimal places for display consistency
        return Double.parseDouble(String.format("%.2f", cgpa)); 
    }
    
    // --- Key Method: Calculates CGPA (Modularity/Abstraction) ---
    public void calculateCGPA() {
        double totalWeightedGradePoints = 0.0; 
        int totalCreditHours = 0;
        this.totalFailedCourses = 0; 
        
        for (Enrollment enrollment : this.enrollments) {
            int credits = enrollment.getCreditHours();
            double gp = enrollment.getGradePoint();

            // Check for failed course (Grade Point 0.0)
            if (gp == 0.0) { 
                this.totalFailedCourses++;
            }
            
            // CGPA Calculation component: (Grade Point * Credit Hours)
            totalWeightedGradePoints += gp * credits;
            totalCreditHours += credits;
        }

        // Final CGPA calculation
        if (totalCreditHours > 0) {
            this.cgpa = totalWeightedGradePoints / totalCreditHours;
        } else {
            this.cgpa = 0.0;
        }
    }
    
    // --- Eligibility Check ---
    public boolean isEligibleToProgress() {
        calculateCGPA(); // Ensure calculations are fresh
        
        // Criteria: CGPA >= 2.0 AND Not more than three failed courses
        return this.cgpa >= 2.0 && this.totalFailedCourses <= 3;
    }
    
    // Helper method for the 'Remarks' section of the report
    public String getAcademicRemarks() {
        if (isEligibleToProgress()) {
            return "Satisfactory. Eligible to progress to the next level of study.";
        } else {
            // Provide a specific reason for ineligibility
            if (this.cgpa < 2.0) {
                return "Action Required: Not eligible due to low CGPA (" + getCgpa() + "). Needs a Course Recovery Plan.";
            } else {
                return "Action Required: Not eligible due to too many failed courses (" + this.totalFailedCourses + "). Needs a Course Recovery Plan.";
            }
        }
    }
}