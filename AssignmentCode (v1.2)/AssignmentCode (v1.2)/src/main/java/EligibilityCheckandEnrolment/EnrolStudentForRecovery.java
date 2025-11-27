/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EligibilityCheckandEnrolment;

import java.awt.Component;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

// IMPORT YOUR EMAIL CLASSES
import com.mycompany.assignmentcode.EmailContainer;
import com.mycompany.assignmentcode.EmailService;

public class EnrolStudentForRecovery {

    private final String MARK_FILE = "StudentMark.txt";
    private final String STUDENT_FILE = "StudentInfo.txt";
    private final String COURSE_FILE = "CourseAssessInfo.txt";

    public EnrolStudentForRecovery() {
    }

    // --- SHARED: Refresh Table Logic ---
    public void refreshTable(JTable table) {
        String[] columns = {"Student Name", "Student ID", "Course Name", "Exam", "Assign", "Grade", "GP"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        ArrayList<String[]> data = getAllEnrolmentData(); 
        for (String[] row : data) {
            model.addRow(row);
        }
        table.setModel(model);
    }

    // --- LOGIC: ADD GRADE ---
    public void addGrade(Component parent, JTable table, JTextField nameField, JTextField idField, JTextField courseField, JTextField examField, JTextField assignField) {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String course = courseField.getText().trim();
        String exam = examField.getText().trim();
        String assign = assignField.getText().trim();

        if (name.isEmpty() || id.isEmpty() || course.isEmpty() || exam.isEmpty() || assign.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please fill all fields.");
            return;
        }

        // 1. Calculate Grade using WEIGHTS
        String[] gradeResult = calculateGradeAndGP(course, exam, assign);
        if (gradeResult == null) {
            JOptionPane.showMessageDialog(parent, "Error: Course ID not found or marks are invalid.");
            return;
        }

        // 2. Save New Student if needed
        if (!checkStudentExists(id)) {
            saveNewStudent(id, name);
        }

        // 3. Check Duplicates
        ArrayList<String[]> allData = loadFile(MARK_FILE);
        for(String[] row : allData) {
            if(row.length > 1 && row[0].trim().equalsIgnoreCase(id) && row[1].trim().equalsIgnoreCase(course)) {
                JOptionPane.showMessageDialog(parent, "Error: Student already enrolled in this course.");
                return;
            }
        }
        
        // 4. Save to File
        String[] newRow = {id, course, exam, assign, gradeResult[0], gradeResult[1]};
        allData.add(newRow);
        saveFile(MARK_FILE, allData);
        
        JOptionPane.showMessageDialog(parent, "Grade Added Successfully!");
        refreshTable(table); 
        
        // Clear Fields
        nameField.setText(""); idField.setText(""); courseField.setText(""); examField.setText(""); assignField.setText("");
    }

    // --- LOGIC: UPDATE GRADE ---
    public void updateGrade(Component parent, JTable table, JTextField idField, JTextField courseField, JTextField examField, JTextField assignField) {
        String id = idField.getText().trim();
        String course = courseField.getText().trim();
        String exam = examField.getText().trim();
        String assign = assignField.getText().trim();

        if (id.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Enter Student ID and Course ID to update.");
            return;
        }
        
        // Calculate Grade using WEIGHTS
        String[] gradeResult = calculateGradeAndGP(course, exam, assign);
        if (gradeResult == null) {
            JOptionPane.showMessageDialog(parent, "Error: Course ID not found or marks are invalid.");
            return;
        }

        ArrayList<String[]> allData = loadFile(MARK_FILE);
        boolean found = false;

        for (int i = 0; i < allData.size(); i++) {
            String[] row = allData.get(i);
            if (row.length > 1 && row[0].trim().equalsIgnoreCase(id) && row[1].trim().equalsIgnoreCase(course)) {
                row[2] = exam;
                row[3] = assign;
                row[4] = gradeResult[0]; 
                row[5] = gradeResult[1]; 
                allData.set(i, row);
                found = true;
                break;
            }
        }
        
        if (found) {
            saveFile(MARK_FILE, allData);
            JOptionPane.showMessageDialog(parent, "Updated Successfully!");
            refreshTable(table);
        } else {
            JOptionPane.showMessageDialog(parent, "Error: Record not found.");
        }
    }

    // --- LOGIC: DELETE GRADE ---
    public void deleteGrade(Component parent, JTable table, JTextField idField, JTextField courseField) {
        String id = idField.getText().trim();
        String course = courseField.getText().trim();

        if (id.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Enter Student ID and Course ID to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(parent, "Delete record?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        ArrayList<String[]> allData = loadFile(MARK_FILE);
        ArrayList<String[]> newData = new ArrayList<>();
        boolean found = false;

        for (String[] row : allData) {
            if (row.length > 1 && row[0].trim().equalsIgnoreCase(id) && row[1].trim().equalsIgnoreCase(course)) {
                found = true; 
            } else {
                newData.add(row);
            }
        }
        
        if (found) {
            saveFile(MARK_FILE, newData);
            JOptionPane.showMessageDialog(parent, "Deleted Successfully!");
            refreshTable(table); 
        } else {
            JOptionPane.showMessageDialog(parent, "Error: Record not found.");
        }
    }

    // --- LOGIC: SEND EMAIL (Updated to use EmailService) ---
    public void sendRecoveryEmail(Component parent, JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(parent, "Please select a row.");
            return;
        }
        String name = table.getValueAt(row, 0).toString(); 
        String course = table.getValueAt(row, 2).toString();
        String grade = table.getValueAt(row, 5).toString(); 
        
        // 1. Check if Failed
        boolean isFailed = grade.equalsIgnoreCase("F") || grade.equalsIgnoreCase("D") || grade.equalsIgnoreCase("Fail");
        
        if(isFailed) {
            // 2. Prepare Email Content
            // NOTE: Using a test email for demonstration. 
            // In a real app, you would fetch the student's email from StudentInfo.txt using their ID.
            String targetEmail = "tan28174@gmail.com"; 
            
            String subject = "URGENT: Course Recovery Required - " + course;
            String body = "Dear " + name + ",\n\n" +
                          "This is an automated notification regarding your recent results.\n" +
                          "You have obtained a grade of (Failed) in the course: " + course + ".\n\n" +
                          "As your Grade Point is below 2.0, you are required to retake this subject.\n" +
                          "Please proceed to the Enrolment section to register for the recovery class.\n\n" +
                          "Regards,\n" +
                          "Academic Office";
            
            // 3. Call the Email Service
            try {
                // Polymorphism: Using EmailContainer reference to hold EmailService object
                EmailContainer mailer = new EmailService();
                
                // This sends the email using the logic in your EmailService class
                mailer.sendEmail(targetEmail, subject, body);
                
                JOptionPane.showMessageDialog(parent, "Email notification sent to " + targetEmail);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Error initiating email service: " + e.getMessage());
                e.printStackTrace();
            }
            
        } else {
            JOptionPane.showMessageDialog(parent, "Student passed (" + grade + "). No email required.");
        }
    }

    // --- HELPERS (Private) ---
    
    // Calculates based on CourseAssessInfo.txt weights
    private String[] calculateGradeAndGP(String courseID, String examStr, String assignStr) {
        try {
            double exam = Double.parseDouble(examStr);
            double assign = Double.parseDouble(assignStr);
            
            int[] weights = getCourseWeights(courseID);
            if (weights == null) return null; // Course not found
            
            double finalScore = (exam * weights[0] / 100.0) + (assign * weights[1] / 100.0);
            
            String grade = (finalScore >= 80) ? "A" : (finalScore >= 70) ? "B" : (finalScore >= 60) ? "C" : (finalScore >= 50) ? "D" : "F";
            String point = (finalScore >= 80) ? "4.0" : (finalScore >= 70) ? "3.0" : (finalScore >= 60) ? "2.0" : (finalScore >= 50) ? "1.0" : "0.0";
            return new String[]{grade, point};
        } catch (Exception e) { return null; }
    }

    // Reads weights like "60,40" from file
    private int[] getCourseWeights(String courseID) {
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_FILE))) {
            String line; br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 7 && d[0].trim().equalsIgnoreCase(courseID.trim())) {
                    return new int[]{ Integer.parseInt(d[5].trim()), Integer.parseInt(d[6].trim()) };
                }
            }
        } catch (Exception e) {}
        return null;
    }

    private ArrayList<String[]> getAllEnrolmentData() {
        ArrayList<String[]> resultList = new ArrayList<>();
        ArrayList<String[]> marks = loadFile(MARK_FILE);
        ArrayList<String[]> students = loadFile(STUDENT_FILE);
        ArrayList<String[]> courses = loadFile(COURSE_FILE);

        for (String[] mark : marks) {
            if (mark.length < 6) continue;
            String stdID = mark[0].trim();
            String crsID = mark[1].trim();

            String name = "Unknown";
            for (String[] s : students) {
                if (s.length > 1 && s[0].trim().equalsIgnoreCase(stdID)) { name = s[1] + " " + s[2]; break; }
            }
            String courseName = crsID; 
            for(String[] c : courses) {
                if(c.length > 1 && c[0].trim().equalsIgnoreCase(crsID)) { courseName = c[1]; break; }
            }
            resultList.add(new String[]{ name, stdID, courseName, mark[2], mark[3], mark[4], mark[5] });
        }
        return resultList;
    }

    private boolean checkStudentExists(String id) {
        ArrayList<String[]> students = loadFile(STUDENT_FILE);
        for(String[] s : students) {
            if(s.length > 0 && s[0].trim().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    private boolean saveNewStudent(String id, String fullName) {
        String[] nameParts = fullName.trim().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = (nameParts.length > 1) ? nameParts[1] : "-";
        String newLine = id + "," + firstName + "," + lastName + ",Computer Science,Freshman,student@university.edu";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENT_FILE, true))) { 
            bw.write(newLine); bw.newLine(); return true;
        } catch (IOException e) { return false; }
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

    private void saveFile(String filename, ArrayList<String[]> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("StudentID,CourseID,ExamMark,AssignmentMark,FinalGrade,GradePoint");
            bw.newLine();
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}