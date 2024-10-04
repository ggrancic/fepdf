package com.fepdf.fepdf.controlador;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

/**
 * Test Controller class to list PDF form fields.
 * 
 * @author gl
 */
public class testController {
    
    public static void main(String[] args) {
        byte[] byteArray;

        try {
            // Load the PDF from resources as a byte array
            byteArray = IOUtils.toByteArray(testController.class.getResourceAsStream("/pdf/hojaInscripcionNacimiento.pdf"));
            PDDocument docOrig = Loader.loadPDF(byteArray);
            PDAcroForm acroForm = docOrig.getDocumentCatalog().getAcroForm();
           
            if (acroForm != null) {
                for (PDField field : acroForm.getFields()) {
                    System.out.println("Field Name: " + field.getPartialName());
                    System.out.println("Field Type: " + field.getFieldType());
                    System.out.println("Field Value: " + field.getValueAsString());
                    System.out.println("-------------------------");
                }
            } else {
                System.out.println("No AcroForm found in this PDF.");
            }
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
