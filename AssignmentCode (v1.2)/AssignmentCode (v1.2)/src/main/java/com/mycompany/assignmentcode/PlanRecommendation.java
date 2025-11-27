/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author William
 */
public class PlanRecommendation extends StudentCourseRecord {
    private String component;
    private String recommendation;

    public PlanRecommendation(String studentID, String courseID, String component, String recommendation) {
        super(studentID, courseID);
        this.component = component;
        this.recommendation = recommendation;
    }

    public String getComponent() {
        return component;
    }
    
    public String getRecommendation() {
        return recommendation;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
 
    @Override
    public String toString() {
        return studentID + "," + courseID + "," + component + "," + recommendation;
    }
}
