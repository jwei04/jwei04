/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author William
 */
public abstract class Assessment {
    protected String componentName;
    protected int mark;

    public Assessment(String componentName, int mark) {
        this.componentName = componentName;
        this.mark = mark;
    }

    public String getComponentName() {
        return componentName;
    }

    public int getMark() {
        return mark;
    }
    
    public abstract boolean hasFailed();

    @Override
    public String toString() {
        return componentName + ": " + mark;
    }   
}
