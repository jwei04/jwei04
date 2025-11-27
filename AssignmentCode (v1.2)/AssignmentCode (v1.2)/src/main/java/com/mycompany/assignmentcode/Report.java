/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author JIN WEI
 */
public abstract class Report {
    protected Student student;
    public Report(Student student) {
        this.student = student;
    }
    public abstract void generateReport();
}
