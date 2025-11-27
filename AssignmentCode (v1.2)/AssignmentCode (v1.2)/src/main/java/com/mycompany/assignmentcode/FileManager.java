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

public abstract class FileManager<T> {
    public abstract List<T> loadFromFile(String filename);
    
    public void saveToFile(String filename, List<T> data) {
  
    }
}
