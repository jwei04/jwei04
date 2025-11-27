/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Note: Ensure the Course, Enrollment, and Student classes are in this same package.

public class AcademicDataManager {
    
    // HashMaps for fast lookup of data, satisfying the Encapsulation requirement
    private final Map<String, Student> studentMap;
    private final Map<String, Course> courseMap;
    
    // File names for easy reference
    private static final String STUDENT_INFO_FILE = "StudentInfo.txt";
    private static final String COURSE_ASSESS_FILE = "CourseAssessInfo.txt";
    private static final String STUDENT_MARK_FILE = "StudentMark.txt";

    public AcademicDataManager() {
        this.studentMap = new HashMap<>();
        this.courseMap = new HashMap<>();
        
        // Load data in the correct sequence: Static data first, then performance data
        loadCourses();
        loadStudents();
        loadMarksAndConsolidate();
    }
    
    /**
     * Retrieves a Student object by their ID. Required for the GUI search function.
     * @param id The StudentID (e.g., S001).
     * @return The Student object or null if not found.
     */
    public Student getStudentByID(String id) {
        return studentMap.get(id);
    }
    
    /**
     * Helper method to return the full student map. Used primarily for testing purposes.
     * TEMPORARILY added for testing, as requested.
     * @return The Map containing all loaded Student objects.
     */
    public Map<String, Student> getStudentMap() {
        return studentMap;
    }
    
    // -------------------------------------------------------------------------
    // --- Data Loading Methods (File I/O) ---
    // -------------------------------------------------------------------------

    // 1. Reads CourseAssessInfo.txt: CourseID,CourseName,Credits,...
    private void loadCourses() {
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_ASSESS_FILE))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // We need at least the first three parts: ID, Name, Credits
                if (parts.length >= 3) { 
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int credits = Integer.parseInt(parts[2].trim());
                    
                    Course course = new Course(id, name, credits);
                    courseMap.put(id, course);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: Could not read CourseAssessInfo.txt: " + e.getMessage());
        } catch (NumberFormatException e) {
             System.err.println("Error: Failed to parse credit hours in course data: " + e.getMessage());
        }
    }
    
    // 2. Reads StudentInfo.txt: StudentID,FirstName,LastName,Major,Year,Email
    private void loadStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_INFO_FILE))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // We need all 6 parts for student basic info
                if (parts.length >= 6) {
                    Student student = new Student(
                        parts[0].trim(), parts[1].trim(), parts[2].trim(), 
                        parts[3].trim(), parts[4].trim(), parts[5].trim()
                    );
                    studentMap.put(student.getStudentID(), student);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: Could not read StudentInfo.txt: " + e.getMessage());
        }
    }
    
    // 3. Reads StudentMark.txt and links Course and Mark data to the Student objects.
    // StudentID,CourseID,ExamMark,AssignmentMark,FinalGrade,GradePoint
    private void loadMarksAndConsolidate() {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_MARK_FILE))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // We need at least 6 parts
                if (parts.length >= 6) { 
                    String studentId = parts[0].trim();
                    String courseId = parts[1].trim();
                    String finalGrade = parts[4].trim();
                    double gradePoint = Double.parseDouble(parts[5].trim());

                    // --- Consolidation Logic: Check if both linked objects exist ---
                    if (studentMap.containsKey(studentId) && courseMap.containsKey(courseId)) {
                        Student student = studentMap.get(studentId);
                        Course course = courseMap.get(courseId);
                        
                        // Create Enrollment object using data from both static and mark files
                        Enrollment enrollment = new Enrollment(course, finalGrade, gradePoint);
                        
                        // Link the enrollment to the Student (the core consolidation step)
                        student.addEnrollment(enrollment); 
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error: Could not read StudentMark.txt: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Failed to parse GradePoint in student marks: " + e.getMessage());
        }
    }
}