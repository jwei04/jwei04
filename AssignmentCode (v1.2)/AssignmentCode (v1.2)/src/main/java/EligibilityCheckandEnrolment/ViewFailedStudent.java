/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EligibilityCheckandEnrolment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ViewFailedStudent {

    private final String STUDENT_FILE = "StudentInfo.txt";
    private final String MARK_FILE = "StudentMark.txt";
    private final String COURSE_FILE = "CourseAssessInfo.txt";

    public ViewFailedStudent() {
    }

    /**
     * Searches for failed students and prepares the exact row data for the table.
     */
    public ArrayList<String[]> searchFailedRecords(String searchId) {
        ArrayList<String[]> resultList = new ArrayList<>();
        
        // 1. Load all data
        ArrayList<String[]> students = loadFile(STUDENT_FILE);
        ArrayList<String[]> courses = loadFile(COURSE_FILE);
        ArrayList<String[]> marks = loadFile(MARK_FILE);

        // 2. Iterate through marks
        for (String[] markRow : marks) {
            // markRow: [StudentID, CourseID, ExamMark, AssignmentMark, FinalGrade, GradePoint]
            if (markRow.length < 5) continue;
            
            String stdID = markRow[0].trim();
            String crsID = markRow[1].trim();
            String examMark = markRow[2].trim();   // Actual Mark
            String assignMark = markRow[3].trim(); // Actual Mark
            String grade = markRow[4].trim();

            // Filter: Failures only (F or D) + Optional Search ID
            boolean isFailed = grade.equalsIgnoreCase("F") || grade.equalsIgnoreCase("D") || grade.equalsIgnoreCase("Fail");
            boolean matchesSearch = searchId.isEmpty() || stdID.equalsIgnoreCase(searchId);

            if (isFailed && matchesSearch) {
                // 3. Find Student Name
                String stdName = "Unknown";
                for (String[] s : students) {
                    if (s.length > 2 && s[0].trim().equalsIgnoreCase(stdID)) {
                        stdName = s[1] + " " + s[2];
                        break;
                    }
                }

                // 4. Find Course Info (Credit & Instructor)
                String crsName = "Unknown";
                String credit = "0";
                String instructor = "Unknown";

                for (String[] c : courses) {
                    // CourseAssessInfo: [ID, Name, Credit, Semester, Instructor, ...]
                    if (c.length > 4 && c[0].trim().equalsIgnoreCase(crsID)) {
                        crsName = c[1];
                        credit = c[2];      // Index 2 is Credit
                        instructor = c[4];  // Index 4 is Instructor
                        break;
                    }
                }

                // 5. Prepare Row (Matches your Screenshot Columns)
                String[] row = {
                    stdName,      // Student Name
                    stdID,        // Student ID
                    crsName,      // Course Name
                    crsID,        // Course ID
                    credit,       // Credit
                    instructor,   // Instructor
                    examMark,     // Exam Mark
                    assignMark,   // Assignment Mark
                    grade         // Result
                };
                resultList.add(row);
            }
        }
        return resultList;
    }

    private ArrayList<String[]> loadFile(String filename) {
        ArrayList<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true; 
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                if (!line.trim().isEmpty()) data.add(line.split(","));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return data;
    }
}