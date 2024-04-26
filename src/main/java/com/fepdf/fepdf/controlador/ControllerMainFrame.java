package com.fepdf.fepdf.controlador;

import com.fepdf.fepdf.modelo.Persona;
import com.fepdf.fepdf.vista.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDChoice;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class ControllerMainFrame implements ActionListener {
    private MainFrame vista;
    
    public ControllerMainFrame(MainFrame vista) {
        this.vista = vista;
        this.vista.getBtnFin().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Persona personaIngresada = this.captureAttribs();
            this.completarAnexoV(personaIngresada);
            this.completarAnexoIII(personaIngresada);
            this.completarAnexoI(personaIngresada);
            this.completarHojaInscripcion(personaIngresada);
            this.completarDeclaracion(personaIngresada);
            this.completarSolInsc(personaIngresada);
            this.limpiarCampos();
            JOptionPane.showMessageDialog(vista, "Formularios completados", "EXITO", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vista, "La operacion no pudo ser completada", "ERROR", JOptionPane.ERROR_MESSAGE);
        }   
    }
    
    public void btnLimpiarPresionado(ActionEvent e) {
        this.limpiarCampos();
    }
    
    private Persona captureAttribs() {
        Persona persona = new Persona();
        persona.setNombres(this.vista.getCampoNombres().getText().toUpperCase());
        persona.setApellidoPaterno(this.vista.getCampoA1().getText().toUpperCase());
        persona.setApellidoMaterno(this.vista.getCampoA2().getText().toUpperCase());
        persona.setEstadoCivil(this.vista.getCampoEC().getText().toUpperCase());
        persona.setNacionalidad(this.vista.getCampoNacio().getText().toUpperCase());
        persona.setDNI(this.vista.getCampoDNI().getText().toUpperCase());
        persona.setCalle(this.vista.getCampoCalle().getText().toUpperCase());
        persona.setCiudad(this.vista.getCampoCiudad().getText().toUpperCase());
        persona.setProvincia(this.vista.getCampoProvincia().getText().toUpperCase());
        persona.setPais(this.vista.getCampoPais().getText().toUpperCase());
        persona.setTelefono(this.vista.getCampoTelefono().getText());
        persona.setEmail(this.vista.getCampoEmail().getText());
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        persona.setFechaNacimiento(df.format(this.vista.getCampoFN().getDate()));
        
        persona.setConsulado(this.vista.getCampoConsulado().getText().toUpperCase());
        return persona;
    }
    
    private boolean completarAnexoV(Persona persona) throws URISyntaxException {
       boolean ok = false;
       try {
           byte[] byteArray = IOUtils.toByteArray((this.getClass().getResourceAsStream("/pdf/anexo5b.pdf")));       
           PDDocument docOrig = Loader.loadPDF(byteArray);
           PDAcroForm docAcroForm = docOrig.getDocumentCatalog().getAcroForm();
           PDField tboxFormulado = docAcroForm.getField("campoFormulado");
           PDField tboxNombres = docAcroForm.getField("campoNombre");
           PDField tboxAM = docAcroForm.getField("campoAM");
           PDField tboxAP = docAcroForm.getField("campoAP");
           PDField tboxNac = docAcroForm.getField("campoNacionalidad");
           PDField tboxEC = docAcroForm.getField("campoEC");
           PDField tboxDNI = docAcroForm.getField("campoDNI");
           PDField tboxDom = docAcroForm.getField("campoDom");
           PDField tboxProv = docAcroForm.getField("campoProv");
           PDField tboxPais = docAcroForm.getField("campoPais");
           PDField tboxTelefono = docAcroForm.getField("campoTelefono");
           PDField tboxEmail = docAcroForm.getField("campoMail");
           PDField tboxConsulado = docAcroForm.getField("campoConsulado");
           PDField tboxConsulado2 = docAcroForm.getField("campoConsulado2");
           //PDField tboxDiaTurno = docAcroForm.getField("diaTurno");
           //PDField tboxMesTurno = docAcroForm.getField("mesTurno");
           //PDField tboxAnioTurno = docAcroForm.getField("anioTurno");
           
           tboxFormulado.setValue(persona.getNombres());
           tboxNombres.setValue(persona.getNombres());
           tboxAM.setValue(persona.getApellidoMaterno());
           tboxAP.setValue(persona.getApellidoPaterno());
           tboxNac.setValue(persona.getNacionalidad());
           tboxEC.setValue(persona.getEstadoCivil());
           tboxDNI.setValue("DNI: " + persona.getDNI());
           tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad() + ", " + persona.getPais());
           tboxProv.setValue(persona.getProvincia());
           tboxPais.setValue(persona.getPais());
           tboxTelefono.setValue(persona.getTelefono());
           tboxEmail.setValue(persona.getEmail());
           tboxConsulado.setValue(persona.getConsulado());
           tboxConsulado2.setValue(persona.getConsulado());
           
           docOrig.save(this.crearCarpeta(persona) + "/Anexo5.pdf");
           docOrig.close();
           
           ok = true;
       } catch (IOException e) {
           e.printStackTrace();
       }
       return ok;
    }
    
    private boolean completarAnexoIII(Persona persona) throws URISyntaxException {
        boolean ok = false;
        
        try {
           byte[] byteArray = IOUtils.toByteArray((this.getClass().getResourceAsStream("/pdf/anexo3.pdf")));       
           PDDocument docAnexo3 = Loader.loadPDF(byteArray);
           PDAcroForm docAcroForm = docAnexo3.getDocumentCatalog().getAcroForm();
           PDField tboxRC = docAcroForm.getField("campoRC");
           PDField tboxNombres = docAcroForm.getField("campoNombre");
           PDField tboxAM = docAcroForm.getField("campoAM");
           PDField tboxAP = docAcroForm.getField("campoAP");
           PDField tboxNac = docAcroForm.getField("campoNacionalidad");
           PDField tboxEC = docAcroForm.getField("campoEC");
           PDField tboxDNI = docAcroForm.getField("campoDNI");
           PDField tboxDom = docAcroForm.getField("campoDom");
           PDField tboxProv = docAcroForm.getField("campoProv");
           PDField tboxPais = docAcroForm.getField("campoPais");
           PDField tboxTelefono = docAcroForm.getField("campoTelefono");
           PDField tboxEmail = docAcroForm.getField("campoMail");
           PDField tboxConsulado = docAcroForm.getField("campoConsulado");
           PDField checkComun = docAcroForm.getField("checkComun");
           
           tboxRC.setValue(persona.getConsulado());
           tboxNombres.setValue(persona.getNombres());
           tboxAM.setValue(persona.getApellidoMaterno());
           tboxAP.setValue(persona.getApellidoPaterno());
           tboxNac.setValue(persona.getNacionalidad());
           tboxEC.setValue(persona.getEstadoCivil());
           tboxDNI.setValue("DNI: " + persona.getDNI());
           tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad() + ", " + persona.getPais());
           tboxProv.setValue(persona.getProvincia());
           tboxPais.setValue(persona.getPais());
           tboxTelefono.setValue(persona.getTelefono());
           tboxEmail.setValue(persona.getEmail());
           tboxConsulado.setValue(persona.getConsulado());
           ((PDCheckBox) checkComun).check();
           docAnexo3.save(this.crearCarpeta(persona) + "/Anexo3.pdf");
           docAnexo3.close();
           ok = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    }
    
    private boolean completarAnexoI(Persona persona) throws URISyntaxException {
        boolean ok = false;
        
        try {
           byte[] byteArray = IOUtils.toByteArray((this.getClass().getResourceAsStream("/pdf/anexo1.pdf")));       
           PDDocument docAnexo1 = Loader.loadPDF(byteArray);
           PDAcroForm docAcroForm = docAnexo1.getDocumentCatalog().getAcroForm();
           PDField tboxRC = docAcroForm.getField("campoRC");
           PDField tboxNombres = docAcroForm.getField("campoNombre");
           PDField tboxAM = docAcroForm.getField("campoAM");
           PDField tboxAP = docAcroForm.getField("campoAP");
           PDField tboxNac = docAcroForm.getField("campoNacionalidad");
           PDField tboxEC = docAcroForm.getField("campoEC");
           PDField tboxDNI = docAcroForm.getField("campoDNI");
           PDField tboxDom = docAcroForm.getField("campoDom");
           PDField tboxProv = docAcroForm.getField("campoProv");
           PDField tboxPais = docAcroForm.getField("campoPais");
           PDField tboxTelefono = docAcroForm.getField("campoTelefono");
           PDField tboxEmail = docAcroForm.getField("campoMail");
           PDField tboxConsulado = docAcroForm.getField("campoConsulado");
           PDField checkComun = docAcroForm.getField("checkComun");
           PDField checkD1 = docAcroForm.getField("checkD1");
           PDField checkD2 = docAcroForm.getField("checkD2");
           PDField checkD3 = docAcroForm.getField("checkD3");
           PDField checkD4 = docAcroForm.getField("checkD4");
           PDField checkD5 = docAcroForm.getField("checkD5");
           
           tboxRC.setValue(persona.getConsulado());
           tboxNombres.setValue(persona.getNombres());
           tboxAM.setValue(persona.getApellidoMaterno());
           tboxAP.setValue(persona.getApellidoPaterno());
           tboxNac.setValue(persona.getNacionalidad());
           tboxEC.setValue(persona.getEstadoCivil());
           tboxDNI.setValue("DNI: " + persona.getDNI());
           tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad() + ", " + persona.getPais());
           tboxProv.setValue(persona.getProvincia());
           tboxPais.setValue(persona.getPais());
           tboxTelefono.setValue(persona.getTelefono());
           tboxEmail.setValue(persona.getEmail());
           tboxConsulado.setValue(persona.getConsulado());
           ((PDCheckBox) checkComun).check();
           ((PDCheckBox) checkD1).check();
           ((PDCheckBox) checkD2).check();
           ((PDCheckBox) checkD3).check();
           ((PDCheckBox) checkD4).check();
           ((PDCheckBox) checkD5).check();
           
           docAnexo1.save(this.crearCarpeta(persona) + "/Anexo1.pdf");
           docAnexo1.close();
           ok = true;
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return ok;
    }
    
    private boolean completarHojaInscripcion (Persona persona) throws URISyntaxException {
        boolean ok = true;
        
        try {
           byte[] byteArray = IOUtils.toByteArray((this.getClass().getResourceAsStream("/pdf/hojaInscripcionNacimiento.pdf")));       
           PDDocument docHoja = Loader.loadPDF(byteArray);
           PDAcroForm docAcroForm = docHoja.getDocumentCatalog().getAcroForm();
           PDField tboxNombre = docAcroForm.getField("Nombre");
           PDField tboxA1 = docAcroForm.getField("Apellido1");
           PDField tboxA2 = docAcroForm.getField("Apellido2");
           PDField tboxDeclarante = docAcroForm.getField("Declarante");
           PDField tboxNac = docAcroForm.getField("LugarNacDecl");
           PDField tboxFN = docAcroForm.getField("FechaNacDecl");
           PDField tboxDom = docAcroForm.getField("DomicilioDecl");
           PDField tboxTel = docAcroForm.getField("TfnoDecl");
           PDField tboxMail = docAcroForm.getField("MailDecl");
           PDField tboxDNI = docAcroForm.getField("DocumentoDecl");
           PDField tboxCons = docAcroForm.getField("LugarFecha");
           
           tboxNombre.setValue(persona.getNombres());
           tboxA1.setValue(persona.getApellidoPaterno());
           tboxA2.setValue(persona.getApellidoMaterno());
           tboxDeclarante.setValue(persona.getNombres() + " " + persona.getApellidoPaterno());
           tboxNac.setValue(persona.getPais());
           tboxFN.setValue(persona.getFechaNacimiento());
           tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad() + ", " + persona.getPais());
           tboxTel.setValue(persona.getTelefono());
           tboxMail.setValue(persona.getEmail());
           tboxDNI.setValue("DNI: " + persona.getDNI());
           tboxCons.setValue(persona.getConsulado());
           
           docHoja.save(this.crearCarpeta(persona) + "/InscripNacimiento.pdf");
           docHoja.close();
           ok = true;
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return ok;
    }
    
    private boolean completarDeclaracion(Persona persona) throws URISyntaxException {
        boolean ok = false;
        
        try {
           byte[] byteArray = IOUtils.toByteArray((this.getClass().getResourceAsStream("/pdf/declaracionExplicativa.pdf")));       
           PDDocument docDecl = Loader.loadPDF(byteArray);
           PDAcroForm docAcroForm = docDecl.getDocumentCatalog().getAcroForm();
           
           PDField tboxA1 = docAcroForm.getField("topmostSubform[0].Page1[0].apellido1[0]");
           PDField tboxA2 = docAcroForm.getField("topmostSubform[0].Page1[0].apellido2[0]");
           PDField tboxNombres = docAcroForm.getField("topmostSubform[0].Page1[0].nombre[0]");
           PDField tboxDN = docAcroForm.getField("topmostSubform[0].Page1[0].dia\\.nac[0]");
           PDField tboxMN = docAcroForm.getField("topmostSubform[0].Page1[0].mes\\.nac[0]");
           PDField tboxAN = docAcroForm.getField("topmostSubform[0].Page1[0].año\\.nac[0]");
           PDField tboxCiudad = docAcroForm.getField("topmostSubform[0].Page1[0].mun\\.nac[0]");
           PDField tboxProv = docAcroForm.getField("topmostSubform[0].Page1[0].prov\\.nac[0]");
           PDField tboxConsulado = docAcroForm.getField("topmostSubform[0].Page1[0].consulado[0]");
           
           
           tboxA1.setValue(persona.getApellidoPaterno());
           tboxA2.setValue(persona.getApellidoMaterno());
           tboxNombres.setValue(persona.getNombres());
           
           tboxDN.setValue(persona.getFechaNacimiento().substring(0, 2));
           tboxMN.setValue(persona.getFechaNacimiento().substring(3,5));
           tboxAN.setValue(persona.getFechaNacimiento().substring(6, 10));
           
           tboxCiudad.setValue(persona.getCiudad());
           tboxProv.setValue(persona.getProvincia() + (" (ARGENTINA)"));
           tboxConsulado.setValue(persona.getConsulado());
           
           
           
           docDecl.save(this.crearCarpeta(persona) + "/declaracionExplicativa.pdf");
           docDecl.close();
           ok = true;
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return ok;
    }
    
    private boolean completarSolInsc(Persona persona) throws URISyntaxException {
        boolean ok = false;
        
        try {
           byte[] byteArray = IOUtils.toByteArray((this.getClass().getResourceAsStream("/pdf/solicitudInscripcionResidenteEditable.pdf")));       
           PDDocument docSoli = Loader.loadPDF(byteArray);
           PDAcroForm docAcroForm = docSoli.getDocumentCatalog().getAcroForm();
           PDField tboxNom = docAcroForm.getField("tBoxNom");
           PDField tboxA1 = docAcroForm.getField("tBoxApe");
           PDField tboxA2 = docAcroForm.getField("tBoxApe2");
           PDField tboxNac = docAcroForm.getField("tBoxNac");
           PDField tboxTel = docAcroForm.getField("tBoxMovil");
           PDField tboxMail = docAcroForm.getField("tBoxMail");
           PDField tboxC1 = docAcroForm.getField("Text Box 1_41");
           PDField tboxC2 = docAcroForm.getField("Text Box 1_42");
           
           tboxNom.setValue(persona.getNombres());
           tboxA1.setValue(persona.getApellidoPaterno());
           tboxA2.setValue(persona.getApellidoMaterno());
           tboxNac.setValue(persona.getFechaNacimiento());
           tboxTel.setValue(persona.getTelefono());
           tboxMail.setValue(persona.getEmail());
           tboxC1.setValue(persona.getConsulado());
           tboxC2.setValue(persona.getConsulado());
           
           docSoli.save(this.crearCarpeta(persona) + "/SolicitudInscripcionResidente.pdf");
           docSoli.close();
           
           ok = true;
           
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return ok;
    }
    
    private File crearCarpeta(Persona persona) {
        FileSystemView view = FileSystemView.getFileSystemView();
        File file = view.getDefaultDirectory();
        String documentsPath = file.getPath();
        File newDir = new File(documentsPath + "/" + persona.getNombres() + " " + persona.getApellidoPaterno());
        if (!newDir.exists()){
            newDir.mkdirs();
        }
        return newDir;
    }
    
    private void limpiarCampos() {
        this.vista.getCampoNombres().setText("");
        this.vista.getCampoA1().setText("");
        this.vista.getCampoA2().setText("");
        this.vista.getCampoDNI().setText("");
        this.vista.getCampoEC().setText("");
        this.vista.getCampoNacio().setText("");
        this.vista.getCampoCalle().setText("");
        this.vista.getCampoCiudad().setText("");
        this.vista.getCampoProvincia().setText("");
        this.vista.getCampoPais().setText("");
        this.vista.getCampoTelefono().setText("");
        this.vista.getCampoEmail().setText("");
        this.vista.getCampoFN().setCalendar(null);
        this.vista.getCampoConsulado().setText("");
    }
}
