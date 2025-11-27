/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author William
 */
public class RecoveryMilestone extends StudentCourseRecord {
    private String week;
    private String task;

    public RecoveryMilestone(String studentID, String courseID, String week, String task) {
        super(studentID, courseID);
        this.week = week;
        this.task = task;
    }

    public String getWeek() {
        return week;
    }

    public String getTask() {
        return task;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return studentID + "," + courseID + "," + week + "," + task;
    }
    
    
    
}
