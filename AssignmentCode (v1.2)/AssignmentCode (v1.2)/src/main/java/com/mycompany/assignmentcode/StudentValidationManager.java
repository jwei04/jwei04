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

public class StudentValidationManager {
    public boolean isValidStudent(String studentID) {
        try (BufferedReader br = new BufferedReader(new FileReader("StudentInfo.txt"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].trim().equalsIgnoreCase(studentID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading StudentInfo.txt: " + e.getMessage());
        }
        return false;
    }
    
    public boolean isValidCourse (String courseID) {
        try (BufferedReader br = new BufferedReader(new FileReader("CourseAssessInfo.txt"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].trim().equalsIgnoreCase(courseID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CourseAssessInfo.txt: " + e.getMessage());
        }
        return false;
    }
}
