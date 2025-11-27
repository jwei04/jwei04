/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

import java.io.File; // Needed for the File object in the method signature

public abstract class EmailContainer {
    // Keep the original method for text-only emails
    public abstract void sendEmail(String recipient, String subject, String messageText);
    
    // ADD THIS NEW ABSTRACT METHOD for emails with attachments
    public abstract boolean sendEmailWithAttachment(
            String recipient, 
            String subject, 
            String messageText, 
            File attachmentFile); // File object for the PDF
}
