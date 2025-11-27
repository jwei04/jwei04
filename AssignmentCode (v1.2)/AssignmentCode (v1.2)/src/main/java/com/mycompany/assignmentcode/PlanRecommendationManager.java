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

public class PlanRecommendationManager extends FileManager<PlanRecommendation> implements Manager<PlanRecommendation> {
    
    @Override
    public List<PlanRecommendation> loadFromFile (String filename) {
        List<PlanRecommendation> recommendations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("CourseRecoveryPlan.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] parts = line.split(",", 4);
                if (parts.length == 4) {
                    recommendations.add(new PlanRecommendation(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return recommendations;
    }
    
    @Override
    public void saveToFile(String filename, List<PlanRecommendation> recommendations) {
        try (PrintWriter pw = new PrintWriter (new FileWriter("CourseRecoveryPlan.txt", false))) {
            for (PlanRecommendation rec : recommendations) {
                pw.println(rec.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
    
    @Override
    public void addRecord(String filename, PlanRecommendation newRec) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("CourseRecoveryPlan.txt", true))) {
            pw.println(newRec.toString());
        } catch (IOException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }
    
    @Override
    public void updateRecord (String filename, String studentID, String courseID, String newComponent, String newReco) {
        List<PlanRecommendation> recList = loadFromFile("CourseRecoveryPlan.txt");
        for (PlanRecommendation r : recList) {
            if (r.getStudentID().equals(studentID) && r.getCourseID().equals(courseID)) {
                r.setComponent(newComponent);
                r.setRecommendation(newReco);
                break;
            }
        }
        saveToFile(filename, recList);
    }
    
    @Override
    public void removeRecord (String filename, String studentID, String courseID) {
        List<PlanRecommendation> recList = loadFromFile("CourseRecoveryPlan.txt");
        recList.removeIf(r -> r.getStudentID().equals(studentID) && r.getCourseID().equals(courseID));
        saveToFile(filename, recList);
    }
    
    @Override
    public List<PlanRecommendation> searchRecord(String studentID, String courseID, String component, String recommendation) {
        List<PlanRecommendation> allRecs = loadFromFile("CourseRecoveryPlan.txt");
        List<PlanRecommendation> filteredList = new ArrayList();
        
        for (PlanRecommendation rec: allRecs) {
            boolean matchStudentID = studentID.isEmpty() || rec.getStudentID().equalsIgnoreCase(studentID);
            boolean matchCourseID = courseID.isEmpty() || rec.getCourseID().equalsIgnoreCase(courseID);
            boolean matchComponent = component.isEmpty() || rec.getComponent().equalsIgnoreCase(component);
            boolean matchRec = recommendation.isEmpty() || rec.getRecommendation().equalsIgnoreCase(recommendation.toLowerCase());
            
            if (matchStudentID && matchCourseID && matchComponent && matchRec) {
                filteredList.add(rec);
            }
        }
        return filteredList;
    }
    
}
