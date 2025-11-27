/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author William
 */
public class Exam extends Assessment{
    public Exam(int mark) {
        super("Exam", mark);
    }
    
    @Override
    public boolean hasFailed() {
        return mark < 40;
    }
}
