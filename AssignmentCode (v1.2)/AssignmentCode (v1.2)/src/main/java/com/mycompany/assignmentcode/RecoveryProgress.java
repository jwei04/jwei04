/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author William
 */
public class RecoveryProgress extends StudentCourseRecord {
    private String component;
    private String progress;
    private String recoveryMark;

    public RecoveryProgress(String studentID, String courseID, String component, String progress, String recoveryMark) {
        super(studentID, courseID);
        this.component = component;
        this.progress = progress;
        this.recoveryMark = recoveryMark;
    }

    public String getComponent() {
        return component;
    }

    public String getProgress() {
        return progress;
    }

    public String getRecoveryMark() {
        return recoveryMark;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setRecoveryMark(String recoveryMark) {
        this.recoveryMark = recoveryMark;
    }

    @Override
    public String toString() {
        return studentID + "," + courseID + "," + component + "," + progress + "," + recoveryMark;
    }
}
