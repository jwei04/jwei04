/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignmentcode;

// iText Imports (Note: These imports rely on the iText JARs being added to the project)
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import java.io.FileNotFoundException;
import java.util.List;

public class ReportGenerator {

    /**
     * Generates a PDF Academic Performance Report for a given student.
     * This class uses the iText API for PDF creation.
     * @param student The Student object to report on.
     * @param filePath The full path where the PDF should be saved (e.g., "reports/S001_Report.pdf").
     * @return true if the report was created successfully, false otherwise.
     */
    public static boolean generateAcademicReport(Student student, String filePath) {
        if (student == null) {
            System.err.println("Error: Cannot generate report. Student object is null.");
            return false;
        }
        
        // Ensure student CGPA is calculated before generating the report
        student.calculateCGPA(); 

        try {
            // 1. Initialize PDF Writer and Document
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            // Document creation with default size (A4)
            Document document = new Document(pdf); 
            
            // --- HEADER ---
            document.add(new Paragraph("ACADEMIC PERFORMANCE REPORT")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            
            // --- STUDENT DETAILS ---
            document.add(new Paragraph("\nStudent Name: " + student.getName()));
            document.add(new Paragraph("Student ID: " + student.getStudentID()));
            document.add(new Paragraph("Program: " + student.getMajor()));
            document.add(new Paragraph("Year: " + student.getYear()));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Semester 1" ));
            

            // --- COURSE TABLE (Matching the GUI and requirements) ---
            
            // 5 columns with proportional widths
            float[] columnWidths = {1, 3, 1, 1, 1}; 
            Table table = new Table(columnWidths); // FIX: Pass the array directly
            table.setWidth(UnitValue.createPercentValue(100));

            // Helper method to create bold header cell
            java.util.function.Function<String, Cell> createHeaderCell = (text) -> 
                new Cell().add(new Paragraph(text).setBold().setFontSize(10)).setTextAlignment(TextAlignment.CENTER);

            // Add Table Headers
            table.addHeaderCell(createHeaderCell.apply("Course Code"));
            table.addHeaderCell(createHeaderCell.apply("Course Title"));
            table.addHeaderCell(createHeaderCell.apply("Credit Hours"));
            table.addHeaderCell(createHeaderCell.apply("Grade"));
            table.addHeaderCell(createHeaderCell.apply("Grade Point"));
            
            // Add Course Data (Loop through enrollments)
            List<Enrollment> enrollments = student.getEnrollments();
            for (Enrollment enrollment : enrollments) {
                // Add data cells, ensuring GP is formatted to two decimal places
                table.addCell(new Cell().add(new Paragraph(enrollment.getCourseId())));
                table.addCell(new Cell().add(new Paragraph(enrollment.getCourseTitle())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(enrollment.getCreditHours())).setTextAlignment(TextAlignment.CENTER)));
                table.addCell(new Cell().add(new Paragraph(enrollment.getFinalGrade()).setTextAlignment(TextAlignment.CENTER)));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", enrollment.getGradePoint())).setTextAlignment(TextAlignment.RIGHT)));
            }

            document.add(table);

            // --- SUMMARY & REMARKS ---
            document.add(new Paragraph("\nCumulative GPA (CGPA): " + student.getCgpa()).setBold().setFontSize(12));
            document.add(new Paragraph("Remarks: " + student.getAcademicRemarks()));

            // 3. Close Document and Writer
            document.close();
            System.out.println("Report successfully generated: " + filePath);
            return true;

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found or path is invalid: " + filePath);
            return false;
        } catch (Exception e) {
            System.err.println("Failed to generate PDF report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}