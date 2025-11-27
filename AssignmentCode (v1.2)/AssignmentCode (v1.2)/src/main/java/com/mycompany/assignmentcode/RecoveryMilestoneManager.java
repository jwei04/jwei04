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

public class RecoveryMilestoneManager extends FileManager<RecoveryMilestone> implements Manager<RecoveryMilestone>{
    private int targetIndex = -1;
    private StudentManager studentManager = new StudentManager();
    private EmailService emailService = new EmailService();

    public RecoveryMilestoneManager() {
        studentManager.loadFromFile("StudentInfo.txt");
    }
  
    public void setTargetIndex(int index) {
        this.targetIndex = index;
    }
    
    @Override
    public List<RecoveryMilestone> loadFromFile(String filename) {
        List<RecoveryMilestone> milestones = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("RecoveryMilestone.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] parts = line.split(",", 4);
                if (parts.length == 4) {
                    milestones.add(new RecoveryMilestone(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return milestones;
    }
    
    @Override
    public void saveToFile(String filename, List<RecoveryMilestone> milestones) {
        try(PrintWriter pw = new PrintWriter(new FileWriter("RecoveryMilestone.txt", false))) {
            for (RecoveryMilestone milestone : milestones) {
                pw.println(milestone.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
    
    @Override
    public void addRecord(String filename, RecoveryMilestone newRec) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("RecoveryMilestone.txt", true))) {
            pw.println(newRec.toString());
        } catch(IOException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }
    
    @Override
    public void updateRecord(String filename, String studentID, String courseID, String newWeek, String newTask) {
        List<RecoveryMilestone> recList = loadFromFile("RecoveryMilestone.txt");
        
        if (targetIndex >= 0 && targetIndex < recList.size()) {
            RecoveryMilestone m = recList.get(targetIndex);
            m.setWeek(newWeek);
            m.setTask(newTask);
            saveToFile(filename, recList);
        } else {
            // Fallback: if no targetIndex, update the first match (old behavior)
            for (RecoveryMilestone m : recList) {
                if (m.getStudentID().equals(studentID) && m.getCourseID().equals(courseID)) {
                    m.setWeek(newWeek);
                    m.setTask(newTask);
                    break;
                }
            }
        }     
        saveToFile(filename, recList);      
    }
    
    @Override
    public void removeRecord(String filename, String studentID, String courseID) {
       List<RecoveryMilestone> recList = loadFromFile("RecoveryMilestone.txt");
       recList.removeIf(m -> m.getStudentID().equals(studentID) && m.getCourseID().equals(courseID));
       saveToFile(filename, recList);
    }
    
    @Override
    public List<RecoveryMilestone> searchRecord(String studentID, String courseID, String week, String task) {
        List<RecoveryMilestone> allRecs = loadFromFile("RecoveryMilestone.txt");
        List<RecoveryMilestone> filteredList = new ArrayList<>();
        
        for (RecoveryMilestone rec : allRecs) {
            boolean matchStudent = studentID.isEmpty() || rec.getStudentID().equalsIgnoreCase(studentID);
            boolean matchCourse = courseID.isEmpty() || rec.getCourseID().equalsIgnoreCase(courseID);
            boolean matchWeek= week.isEmpty() || rec.getWeek().equalsIgnoreCase(week);
            boolean matchTask = task.isEmpty() || rec.getTask().equalsIgnoreCase(task);
            
            if (matchStudent && matchCourse && matchWeek && matchTask) {
                filteredList.add(rec);
            }
        }
        return filteredList;
    }
    
    public void sendEmailNoti(List<RecoveryMilestone> milestones) {
        if (milestones == null || milestones.isEmpty()) return;

        // Group milestones by student
        Map<String, List<RecoveryMilestone>> studentMap = new HashMap<>();
        for (RecoveryMilestone m : milestones) {
            studentMap.computeIfAbsent(m.getStudentID(), k -> new ArrayList<>()).add(m);
        }

        for (String studentID : studentMap.keySet()) {
            String email = studentManager.getStudentEmail(studentID);
            if (email == null) continue;

            String subject = "Recovery Milestone";

            StringBuilder body = new StringBuilder("Dear Student,\n\nPlease refer to your recovery milestones:\n");
            for (RecoveryMilestone m : studentMap.get(studentID)) {
                body.append("\nCourse: ").append(m.getCourseID())
                    .append(" \n Week: ").append(m.getWeek())
                    .append(" \n Task: ").append(m.getTask())
                    .append("\n");
            }
            body.append("\nPlease follow the recovery plan accordingly.\n\nRegards,\nRecovery System");

            emailService.sendEmail(email, subject, body.toString());
        }
    }
}
