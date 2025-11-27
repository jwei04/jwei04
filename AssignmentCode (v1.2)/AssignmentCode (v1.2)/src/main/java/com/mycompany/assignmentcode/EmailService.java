/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

/**
 *
 * @author William
 */
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.File; 
import javax.activation.DataHandler; 
import javax.activation.FileDataSource;
import java.io.IOException;

public class EmailService extends EmailContainer {
    final String username = "tan28174@gmail.com";  // Your email
    final String password = "ubsv qlty fiwx fwjb";     // App-specific password
    final String host = "smtp.gmail.com";
    final int port = 587;
    
    @Override
    public void sendEmail (String recipient, String subject, String messageText) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host); //SMTP Host
        props.put("mail.smtp.port", String.valueOf(port)); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        
        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            
            message.setContent(messageText, "text/plain; charset=utf-8");
            
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email.");
        }
    }
    
    @Override
    public boolean sendEmailWithAttachment(
            String recipient, 
            String subject, 
            String messageText, 
            File attachmentFile) {
        
        Properties props = new Properties();
        props.put("mail.smtp.host", host); 
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            
            // 1. Create the container for multiple parts (text + attachment)
            MimeMultipart multipart = new MimeMultipart();
            
            // 2. Create the Text Body Part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(messageText, "utf-8");
            
            // 3. Create the Attachment Part (The PDF Report)
            MimeBodyPart attachmentPart = new MimeBodyPart();
            
            // Check if file exists before trying to attach it
            if (!attachmentFile.exists()) {
                throw new MessagingException("Attachment file not found: " + attachmentFile.getAbsolutePath());
            }
            
            FileDataSource fileDataSource = new FileDataSource(attachmentFile);
            
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(attachmentFile.getName());
            
            // 4. Combine the parts
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);
            
            // 5. Set the content and send
            message.setContent(multipart);
            
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipient + " with attachment: " + attachmentFile.getName());
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email. Check username/password/host, or if file exists.");
            return false;
        }
    }
}
