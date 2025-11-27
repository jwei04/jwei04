/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.assignmentcode;

import javax.swing.SwingUtilities;

/**
 *
 * @author tan28
 */
public class EligibilityCheckAndEnrolment {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Run the GUI safely
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // CHANGE THIS LINE: use your exact class name "VIew_failed_student"
                new VIew_failed_student().setVisible(true);
            }
        });
    }
    
}
