/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *  
 * @author William
 */
import java.util.*;

public class StudentMark extends StudentCourseRecord {
    private String finalGrade;
    private double gradePoint;
    private List<Assessment> components;

    public StudentMark(String studentID, String courseID, int examMark, int assignmentMark, String finalGrade, double gradePoint) {
        super(studentID, courseID);
        this.finalGrade = finalGrade;
        this.gradePoint = gradePoint;

        // store the components (exam + assignment)
        components = new ArrayList<>();
        components.add(new Exam(examMark));
        components.add(new Assignment(assignmentMark));
    }
    
    public String getFinalGrade() {
        return finalGrade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public List<Assessment> getFailedComponents() {
        List<Assessment> failed = new ArrayList<>();
        for (Assessment c : components) {
            if (c.hasFailed()) {
                failed.add(c);
            }
        }
        return failed;
    }

    @Override
    public String toString() {
        return studentID + " - " + courseID + " (" + finalGrade + ")";
    }

}
