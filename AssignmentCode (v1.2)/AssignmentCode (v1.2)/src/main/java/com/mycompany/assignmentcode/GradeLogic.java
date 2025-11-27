/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.mycompany.assignmentcode;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class GradeLogic {
//
//    // --- FILE PATHS ---
//    private final String MARK_FILE = "C:\\Users\\tan28\\OneDrive\\Documents\\NetBeansProjects\\Eligibility Check and Enrolment\\src\\StudentMark.txt";
//    private final String INFO_FILE = "C:\\Users\\tan28\\OneDrive\\Documents\\NetBeansProjects\\Eligibility Check and Enrolment\\src\\StudentInfo.txt";
//    private final String COURSE_FILE = "C:\\Users\\tan28\\OneDrive\\Documents\\NetBeansProjects\\Eligibility Check and Enrolment\\src\\CourseAssessInfo.txt";
//
//    // ==========================================================
//    // SEND RECOVERY EMAIL LOGIC (Updated)
//    // ==========================================================
//    public String sendRecoveryEmail(String name, String courseName, String status, String targetEmail) {
//        
//        // 1. Check if they failed (F or D)
//        boolean isFailed = status.equalsIgnoreCase("Failed") || status.equalsIgnoreCase("F") || status.equalsIgnoreCase("D");
//
//        if (isFailed) {
//            // 2. Construct Message
//            String subject = "URGENT: Course Recovery Required - " + courseName;
//            String body = "Dear " + name + ",\n\n" +
//                          "This is an automated notification regarding your recent results.\n" +
//                          "You have obtained a grade of (" + status + ") in the course: " + courseName + ".\n\n" +
//                          "As your Grade Point is below 2.0, you are required to retake this subject.\n" +
//                          "Please proceed to the Enrolment section to register for the recovery class.\n\n" +
//                          "Regards,\nAcademic Office";
//
//            // 3. Use POLYMORPHISM to send email
//            // We create EmailService, but store it in EmailContainer (Parent class)
//            EmailContainer mailer = new EmailService();
//            
//            try {
//                System.out.println("Sending email to: " + targetEmail);
//                mailer.sendEmail(targetEmail, subject, body);
//                return "Email sent successfully to " + targetEmail;
//            } catch (Exception e) {
//                return "Error sending email: " + e.getMessage();
//            }
//        } else {
//            return "Student passed (" + status + "). No email required.";
//        }
//    }
//
//    // ==========================================================
//    // EXISTING METHODS (No changes needed below here)
//    // ==========================================================
//    
//    public ArrayList<String[]> getAllMarksForTable() {
//        return readMarksFromFile("", false, false); 
//    }
//
//    public ArrayList<String[]> searchStudent(String searchID) {
//        return readMarksFromFile(searchID, true, true); 
//    }
//
//    private ArrayList<String[]> readMarksFromFile(String filterID, boolean onlyFailures, boolean detailedView) {
//        ArrayList<String[]> rows = new ArrayList<>();
//        HashMap<String, String> namesMap = loadStudentNames();
//        HashMap<String, String[]> courseDetailsMap = loadDetailedCourseInfo();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(MARK_FILE))) {
//            String line;
//            br.readLine(); 
//            while ((line = br.readLine()) != null) {
//                if (line.trim().isEmpty()) continue;
//                String[] data = line.split(",");
//                
//                if (data.length >= 6) { 
//                    String id = data[0].trim();
//                    String courseID = data[1].trim();
//                    String exam = data[2].trim();
//                    String assign = data[3].trim();
//                    String grade = data[4].trim();
//                    String point = data[5].trim();
//                    
//                    if (!filterID.isEmpty() && !id.equalsIgnoreCase(filterID.trim())) continue;
//
//                    boolean isFailed = grade.equalsIgnoreCase("F") || grade.equalsIgnoreCase("D");
//
//                    if (onlyFailures && !isFailed) continue; 
//
//                    String name = namesMap.getOrDefault(id, "Unknown");
//                    String displayStatus = isFailed ? "Failed" : "Pass";
//                    
//                    String[] cDetails = courseDetailsMap.getOrDefault(courseID, new String[]{courseID, "-", "-", "-", "0", "0"});
//
//                    if (detailedView) {
//                        rows.add(new String[] { name, id, cDetails[0], courseID, cDetails[1], cDetails[2], cDetails[4], cDetails[5], displayStatus });
//                    } else {
//                        rows.add(new String[] { name, id, cDetails[0], exam, assign, grade, point });
//                    }
//                }
//            }
//        } catch (Exception e) { System.out.println("Read Error: " + e.getMessage()); }
//        return rows;
//    }
//    
//    public String addStudent(String name, String id, String courseID, String examStr, String assignStr) {
//        updateStudentInfoFile(id, name);
//        String[] result = calculateGrade(courseID, examStr, assignStr);
//        if (result == null) return "Error: Invalid Marks or Course ID not found.";
//        
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MARK_FILE, true))) {
//            String newLine = id + "," + courseID + "," + examStr + "," + assignStr + "," + result[0] + "," + result[1];
//            bw.write(newLine);
//            bw.newLine();
//            return "Success";
//        } catch (Exception e) { return "Error: " + e.getMessage(); }
//    }
//
//    public boolean updateStudentGrade(String name, String id, String courseID, String examStr, String assignStr) {
//        ArrayList<String> lines = new ArrayList<>();
//        boolean found = false;
//        updateStudentInfoFile(id, name);
//        String[] result = calculateGrade(courseID, examStr, assignStr);
//        if (result == null) return false;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(MARK_FILE))) {
//            String line;
//            if ((line = br.readLine()) != null) lines.add(line); 
//            while ((line = br.readLine()) != null) {
//                if (line.trim().isEmpty()) continue;
//                String[] data = line.split(",");
//                if (data.length >= 6 && data[0].trim().equalsIgnoreCase(id.trim()) && data[1].trim().equalsIgnoreCase(courseID.trim())) {
//                    lines.add(id + "," + courseID + "," + examStr + "," + assignStr + "," + result[0] + "," + result[1]);
//                    found = true;
//                } else {
//                    lines.add(line);
//                }
//            }
//        } catch (Exception e) { return false; }
//
//        if (found) writeLinesToFile(lines);
//        return found;
//    }
//
//    public boolean deleteStudent(String id, String courseID) {
//        ArrayList<String> lines = new ArrayList<>();
//        boolean found = false;
//        try (BufferedReader br = new BufferedReader(new FileReader(MARK_FILE))) {
//            String line;
//            if ((line = br.readLine()) != null) lines.add(line); 
//            while ((line = br.readLine()) != null) {
//                if (line.trim().isEmpty()) continue;
//                String[] data = line.split(",");
//                if (data.length >= 2 && data[0].trim().equalsIgnoreCase(id.trim()) && data[1].trim().equalsIgnoreCase(courseID.trim())) {
//                    found = true; 
//                } else {
//                    lines.add(line);
//                }
//            }
//        } catch (Exception e) { return false; }
//
//        if (found) writeLinesToFile(lines);
//        return found;
//    }
//
//    private void writeLinesToFile(ArrayList<String> lines) {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MARK_FILE))) {
//            for (String s : lines) { bw.write(s); bw.newLine(); }
//        } catch (Exception e) {}
//    }
//
//    private String[] calculateGrade(String courseID, String examStr, String assignStr) {
//        try {
//            double exam = Double.parseDouble(examStr);
//            double assign = Double.parseDouble(assignStr);
//            int[] weights = getCourseWeights(courseID);
//            if (weights == null) return null;
//
//            double finalScore = (exam * weights[0] / 100.0) + (assign * weights[1] / 100.0);
//            String grade = (finalScore >= 80) ? "A" : (finalScore >= 70) ? "B" : (finalScore >= 60) ? "C" : (finalScore >= 50) ? "D" : "F";
//            String point = (finalScore >= 80) ? "4.0" : (finalScore >= 70) ? "3.0" : (finalScore >= 60) ? "2.0" : (finalScore >= 50) ? "1.0" : "0.0";
//            return new String[]{grade, point};
//        } catch (Exception e) { return null; }
//    }
//
//    private int[] getCourseWeights(String courseID) {
//        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_FILE))) {
//            String line; br.readLine();
//            while ((line = br.readLine()) != null) {
//                String[] d = line.split(",");
//                if (d.length >= 7 && d[0].trim().equalsIgnoreCase(courseID.trim())) {
//                    return new int[]{ Integer.parseInt(d[5].trim()), Integer.parseInt(d[6].trim()) };
//                }
//            }
//        } catch (Exception e) {}
//        return null;
//    }
//
//    private void updateStudentInfoFile(String id, String name) {
//        ArrayList<String> lines = new ArrayList<>();
//        boolean idFound = false;
//        try (BufferedReader br = new BufferedReader(new FileReader(INFO_FILE))) {
//            String line;
//            if ((line = br.readLine()) != null) lines.add(line); 
//            while ((line = br.readLine()) != null) {
//                String[] d = line.split(",");
//                if (d.length > 0 && d[0].trim().equalsIgnoreCase(id)) {
//                    String[] np = name.split(" ");
//                    String fn = np.length > 0 ? np[0] : name;
//                    String ln = np.length > 1 ? np[1] : "-";
//                    lines.add(id + "," + fn + "," + ln + ",General,Year1," + id + "@uni.edu");
//                    idFound = true;
//                } else { lines.add(line); }
//            }
//        } catch (Exception e) {}
//        if (!idFound) {
//            String[] np = name.split(" ");
//            String fn = np.length > 0 ? np[0] : name;
//            String ln = np.length > 1 ? np[1] : "-";
//            lines.add(id + "," + fn + "," + ln + ",General,Year1," + id + "@uni.edu");
//        }
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INFO_FILE))) {
//            for (String s : lines) { bw.write(s); bw.newLine(); }
//        } catch (Exception e) {}
//    }
//
//    private HashMap<String, String[]> loadDetailedCourseInfo() {
//        HashMap<String, String[]> map = new HashMap<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_FILE))) {
//            String line; br.readLine();
//            while ((line = br.readLine()) != null) {
//                String[] d = line.split(",");
//                if(d.length >= 7) map.put(d[0].trim(), new String[]{ d[1], d[2], d[4], d[3], d[5], d[6] });
//            }
//        } catch (Exception e) {}
//        return map;
//    }
//    
//    private HashMap<String, String> loadStudentNames() {
//        HashMap<String, String> map = new HashMap<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(INFO_FILE))) {
//            String line; br.readLine();
//            while ((line = br.readLine()) != null) {
//                String[] d = line.split(",");
//                if(d.length>=3) map.put(d[0].trim(), d[1] + " " + d[2]);
//            }
//        } catch (Exception e) {}
//        return map;
//    }
//    
//    private HashMap<String, String> loadCourseNames() {
//        HashMap<String, String> map = new HashMap<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_FILE))) {
//            String line; br.readLine();
//            while ((line = br.readLine()) != null) {
//                String[] d = line.split(",");
//                if(d.length>=2) map.put(d[0].trim(), d[1]);
//            }
//        } catch (Exception e) {}
//        return map;
//    }
//}