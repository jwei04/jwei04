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

public class StudentManager extends FileManager<Student> {
    private List<Student> studentList = new ArrayList<>();
    
    @Override
    public List<Student> loadFromFile(String filename) {
       List<Student> students = new ArrayList<>();
       try (BufferedReader br = new BufferedReader(new FileReader("StudentInfo.txt"))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String [] parts = line.split(",", 6);
                if (parts.length == 6) {
                    students.add(new Student(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        this.studentList = students;
        return students;
    }
    
    public Student getStudentName(String studentID) {
        for (Student s: studentList) {
            if (s.getStudentID().equalsIgnoreCase(studentID)) {
                return s;
            }
        }
        return null;
    }
    
    public String getStudentEmail(String studentID) {
        Student student = getStudentName(studentID);
        return (student != null) ? student.getEmail() : null;
    }
}
