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

public class RecoveryProgressManager extends FileManager<RecoveryProgress> implements Manager<RecoveryProgress> {
    
    @Override
    public List<RecoveryProgress> loadFromFile(String filename) {
       List<RecoveryProgress> progresses = new ArrayList<>();
       try (BufferedReader br = new BufferedReader(new FileReader("RecoveryProgress.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] parts = line.split(",", 5);
                if (parts.length == 5) {
                    progresses.add(new RecoveryProgress(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return progresses;
    }
    
    @Override
    public void saveToFile(String filename, List<RecoveryProgress> progresses) {
        try(PrintWriter pw = new PrintWriter(new FileWriter("RecoveryProgress.txt", false))) {
            for (RecoveryProgress progress : progresses) {
                pw.println(progress.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
    
    @Override
    public void addRecord(String filename, RecoveryProgress newRec) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("RecoveryProgress.txt", true))) {
            pw.println(newRec.toString());
        } catch(IOException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }
    
    @Override
    public void updateRecord(String filename, String studentID, String courseID, String component, String updateData) {
        List<RecoveryProgress> recList = loadFromFile("RecoveryProgress.txt");
        String[] updates = updateData.split("\\|");
        
        String newProgress = updates[0];
        String newMark = updates.length > 1 ? updates[1] : "";
        
        for (RecoveryProgress r : recList) {
            if (r.getStudentID().equals(studentID) && r.getCourseID().equals(courseID) && r.getComponent().equalsIgnoreCase(component)) {
                r.setProgress(newProgress);
                r.setRecoveryMark(newMark);
                break;
            }
        }
        saveToFile(filename, recList);
    }
    
    @Override
    public void removeRecord(String filename, String studentID, String courseID) {
        List<RecoveryProgress> recList = loadFromFile("RecoveryProgress.txt");
        recList.removeIf(p -> p.getStudentID().equals(studentID) && p.getCourseID().equals(courseID));
        saveToFile(filename, recList);
    }
    
    @Override
    public List<RecoveryProgress> searchRecord(String studentID, String courseID, String component, String progress) {
        List<RecoveryProgress> allRecs = loadFromFile("RecoveryProgress.txt");
        List<RecoveryProgress> filteredList = new ArrayList<>();
        
        for (RecoveryProgress rec : allRecs ) {
            boolean matchStudent = studentID.isEmpty() || rec.getStudentID().equalsIgnoreCase(studentID);
            boolean matchCourse = courseID.isEmpty() || rec.getCourseID().equalsIgnoreCase(courseID);
            boolean matchComponent = component.isEmpty() || rec.getComponent().equalsIgnoreCase(component);
            boolean matchProgress = progress.isEmpty() || rec.getProgress().equalsIgnoreCase(progress);
            
            if (matchStudent && matchCourse && matchComponent && matchProgress) {
                filteredList.add(rec);
            }
        }
        return filteredList;
    }
}
