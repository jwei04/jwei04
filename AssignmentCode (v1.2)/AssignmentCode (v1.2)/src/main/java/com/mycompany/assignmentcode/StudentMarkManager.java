/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author William
 */

import java.io.*;
import java.util.*;

public class StudentMarkManager extends FileManager<StudentMark>{
    private List<StudentMark> studentMarks;

    public StudentMarkManager() {
        studentMarks = new ArrayList<>();
    }
    
    @Override
    public List<StudentMark> loadFromFile (String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader("StudentMark.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] parts = line.split(",");
                if (parts.length == 6) {
                    String studentID = parts[0].trim();
                    String courseID = parts[1].trim();
                    int examMark = Integer.parseInt(parts[2].trim());
                    int assignmentMark = Integer.parseInt(parts[3].trim());
                    String finalGrade = parts[4].trim();
                    double gradePoint = Double.parseDouble(parts[5].trim());

                    StudentMark sm = new StudentMark(studentID, courseID, examMark, assignmentMark, finalGrade, gradePoint);
                    studentMarks.add(sm);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return studentMarks;
    }
    
    // Get all failed components for a given student
    public List<String[]> getAllFailedComponents() {
        List<String[]> failedList = new ArrayList<>();

        for (StudentMark sm : studentMarks) {
            for (Assessment c : sm.getFailedComponents()) {
                failedList.add(new String[]{
                    sm.getStudentID(),
                    sm.getCourseID(),
                    c.getComponentName(),
                    String.valueOf(c.getMark()),
                    sm.toString()
                });
            }
        }

        return failedList;
    }
}
