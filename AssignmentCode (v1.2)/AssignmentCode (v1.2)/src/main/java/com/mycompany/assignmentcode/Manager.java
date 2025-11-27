/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author William
 */
import java.util.List;

public interface Manager<T> {
    void addRecord(String filename, T record);
    void updateRecord(String filename, String studentID, String courseID, String field3, String field4);
    void removeRecord(String filename, String studentID, String courseID);
    List<T> searchRecord(String studentID, String courseID, String field3, String field4);
}
