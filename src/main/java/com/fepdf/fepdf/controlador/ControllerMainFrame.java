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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;

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
        persona.setEstadoCivil(this.vista.comboEC.getSelectedItem().toString().toUpperCase());
        persona.setNacionalidad(this.vista.getCampoNacio().getText().toUpperCase());
        persona.setDNI(this.vista.getCampoDNI().getText().toUpperCase());
        persona.setCalle(this.vista.getCampoCalle().getText().toUpperCase());
        persona.setCiudad(this.vista.getCampoCiudad().getText().toUpperCase());
        persona.setProvincia(this.vista.getCampoProvincia().getText().toUpperCase());
        persona.setPais(this.vista.getCampoPais().getText().toUpperCase());
        persona.setTelefono(this.vista.getCampoTelefono().getText());
        persona.setEmail(this.vista.getCampoEmail().getText().toLowerCase());
        persona.setSexo(this.vista.comboSx.getSelectedItem().toString().toUpperCase());
        persona.setMunEsp(this.vista.campoME.getText().toUpperCase());
        persona.setProvEsp(this.vista.campoPE.getText().toUpperCase());
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        
        persona.setFechaNacimiento(df.format(this.vista.getCampoFN().getDate()));
        
        persona.setFechaTurno(dft.format(this.vista.campoFT.getDate()));
        
        persona.setConsulado(this.vista.getCampoConsulado().getText().toUpperCase());
        
        persona.setCiudadNacimiento(this.vista.getCampoCN().getText().toUpperCase());
        persona.setProvinciaNacimiento(this.vista.getCampoPrN().getText().toUpperCase());
        persona.setPaisNacimiento(this.vista.getCampoPN().getText().toUpperCase());
        return persona;
    }
    
    private boolean completarAnexoV(Persona persona) throws URISyntaxException {
       boolean ok = false;
       try {
           byte[] byteArray = IOUtils.toByteArray((this.getClass().getResourceAsStream("/pdf/anexo5c.pdf")));       
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
           PDField tboxDiaTurno = docAcroForm.getField("diaTurno");
           PDField tboxMesTurno = docAcroForm.getField("mesTurno");
           PDField tboxAgnoTurno = docAcroForm.getField("agnoTurno");
           PDField tboxDiaTurno2 = docAcroForm.getField("diaTurno2");
           PDField tboxMesTurno2 = docAcroForm.getField("mesTurno2");
           PDField tboxAgnoTurno2 = docAcroForm.getField("agnoTurno2");
           PDField tboxConsulado2 = docAcroForm.getField("campoConsulado2");
           
           tboxFormulado.setValue(persona.getNombres());
           tboxNombres.setValue(persona.getNombres());
           tboxAM.setValue(persona.getApellidoMaterno());
           tboxAP.setValue(persona.getApellidoPaterno());
           tboxNac.setValue(persona.getNacionalidad());
           tboxEC.setValue(persona.getEstadoCivil());
           tboxDNI.setValue("DNI: " + persona.getDNI());
           tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad());
           tboxProv.setValue(persona.getProvincia());
           tboxPais.setValue(persona.getPais());
           tboxTelefono.setValue(persona.getTelefono());
           tboxEmail.setValue(persona.getEmail());
           tboxConsulado.setValue(persona.getConsulado());
            
           LocalDate fechaTurno = LocalDate.parse(persona.getFechaTurno());
           
           tboxDiaTurno.setValue(fechaTurno.getDayOfMonth() + "");
           tboxMesTurno.setValue(fechaTurno.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
           tboxAgnoTurno.setValue(fechaTurno.getYear() + "");
           
           tboxDiaTurno2.setValue(fechaTurno.getDayOfMonth() + "");
           tboxMesTurno2.setValue(fechaTurno.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
           tboxAgnoTurno2.setValue(fechaTurno.getYear() + "");
           
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
           PDField tboxProgInscrito = docAcroForm.getField("Progenitor");
           PDField tboxFechaOpcion = docAcroForm.getField("FechaOpcion");
           PDField check1 = docAcroForm.getField("Casilla de verificación 10");
           PDField check2 = docAcroForm.getField("Casilla de verificación 11");
           PDField check3 = docAcroForm.getField("Casilla de verificación 12");
           PDField check4 = docAcroForm.getField("Casilla de verificación 13");
           PDField tboxOtros = docAcroForm.getField("Otros documentos");
           
           
           tboxRC.setValue(persona.getConsulado());
           tboxNombres.setValue(persona.getNombres());
           tboxAM.setValue(persona.getApellidoMaterno());
           tboxAP.setValue(persona.getApellidoPaterno());
           tboxNac.setValue(persona.getNacionalidad());
           tboxEC.setValue(persona.getEstadoCivil());
           tboxDNI.setValue("DNI: " + persona.getDNI());
           tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad());
           tboxProv.setValue(persona.getProvincia());
           tboxProgInscrito.setValue("ROSARIO");
           
           SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
           try {
               tboxFechaOpcion.setValue(sdf.format(fromUser.parse(persona.getFechaTurno())));
           } catch (ParseException e) {
                e.printStackTrace();
           }
           
           tboxPais.setValue(persona.getPais());
           tboxTelefono.setValue(persona.getTelefono());
           tboxEmail.setValue(persona.getEmail());
           tboxConsulado.setValue(persona.getConsulado());
           ((PDCheckBox) checkComun).check();
           ((PDCheckBox) check1).check();
           ((PDCheckBox) check2).check();
           ((PDCheckBox) check3).check();
           ((PDCheckBox) check4).check();
           
           if (this.vista.combo1.getSelectedItem().toString().equals("SI")) {
               tboxOtros.setValue("ACTA DE MATRIMONIO DE LOS PROGENITORES");
           } else {
               tboxOtros.setValue("AVERIGUAR");
           }
           
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
           PDField tboxAbuelo = docAcroForm.getField("Taextfield-6");
           PDField tboxDiaTurno = docAcroForm.getField("Texto1aa");
           PDField tboxMesTurno = docAcroForm.getField("Texto1ss");
           PDField tboxAgnoTurno = docAcroForm.getField("Texto1dd");
           
           tboxRC.setValue(persona.getConsulado());
           tboxNombres.setValue(persona.getNombres());
           tboxAM.setValue(persona.getApellidoMaterno());
           tboxAP.setValue(persona.getApellidoPaterno());
           tboxNac.setValue(persona.getNacionalidad());
           tboxEC.setValue(persona.getEstadoCivil());
           tboxDNI.setValue("DNI: " + persona.getDNI());
           tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad());
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
           tboxAbuelo.setValue("ESPAÑOLA");
           
           LocalDate fechaTurno = LocalDate.parse(persona.getFechaTurno());
           
           tboxDiaTurno.setValue(fechaTurno.getDayOfMonth() + "");
           tboxMesTurno.setValue(fechaTurno.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
           tboxAgnoTurno.setValue(fechaTurno.getYear() + "");
           
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
           PDField tboxSexo = docAcroForm.getField("Sexo");
           PDField checkM = docAcroForm.getField("varon");
           PDField checkF = docAcroForm.getField("mujer");
           PDField tboxDeclarante = docAcroForm.getField("Declarante");
           PDField tboxNac = docAcroForm.getField("LugarNacDecl");
           PDField tboxFN = docAcroForm.getField("FechaNacDecl");
           PDField tboxDom = docAcroForm.getField("DomicilioDecl");
           PDField tboxTel = docAcroForm.getField("TfnoDecl");
           PDField tboxMail = docAcroForm.getField("MailDecl");
           PDField tboxDNI = docAcroForm.getField("DocumentoDecl");
           PDField tboxCons = docAcroForm.getField("LugarFecha");
           PDField tboxLN = docAcroForm.getField("LugarNacimiento");
           PDField tboxInt = docAcroForm.getField("ParentescoDecl");
           PDField tboxCP = docAcroForm.getField("CPDecl");
           PDField DiaFecha = docAcroForm.getField("DiaFecha");
           PDField MesFecha = docAcroForm.getField("MesFecha");
           PDField AnioFecha = docAcroForm.getField("AnioFecha");
           PDField horaNac = docAcroForm.getField("Hora");
           PDField diaNac = docAcroForm.getField("DiaNacimiento");
           PDField mesNac = docAcroForm.getField("MesNacimiento");
           PDField anoNac = docAcroForm.getField("AnioNacimiento");
           PDField lugarNac = docAcroForm.getField("LugarNacimiento");
           PDField fechaInsc = docAcroForm.getField("FechaInscripcion");
           PDField registroCivil = docAcroForm.getField("RegistroCivil");
           PDField tomo = docAcroForm.getField("Tomo");
           PDField pagina = docAcroForm.getField("Pagina");
           PDField numero = docAcroForm.getField("Numero");
           
           tboxNombre.setValue(persona.getNombres());
           tboxA1.setValue(persona.getApellidoPaterno());
           tboxA2.setValue(persona.getApellidoMaterno());
           tboxSexo.setValue(persona.getSexo());
           
           if (persona.getSexo().equals("MASCULINO")) {
               ((PDCheckBox) checkM).check();
               tboxInt.setValue("INTERESADO");
           } else {
               ((PDCheckBox) checkF).check();
               tboxInt.setValue("INTERESADA");
           }
           SimpleDateFormat sdfn = new SimpleDateFormat("yyyy-MM-dd");
           String formateada = sdfn.format(this.vista.getCampoFN().getDate());
           LocalDate fechaNacimiento = LocalDate.parse(formateada);
           
           
           horaNac.setValue(this.vista.campoHN.getText());
           diaNac.setValue(this.numeroADia(fechaNacimiento.getDayOfMonth()).toUpperCase());
           mesNac.setValue(fechaNacimiento.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
           anoNac.setValue(String.valueOf(fechaNacimiento.getYear()));
           
           lugarNac.setValue(persona.getCiudadNacimiento() + ", " + persona.getProvincia() );
           
           DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
           
           fechaInsc.setValue(df.format(this.vista.campoFI.getDate()));
           registroCivil.setValue(this.vista.campoRC.getText().toUpperCase());
           tomo.setValue(this.vista.campoTomo.getText());
           pagina.setValue(this.vista.campoPagina.getText());
           numero.setValue(this.vista.campoNroPag.getText());
           
           tboxDeclarante.setValue(persona.getNombres() + " " + persona.getApellidoPaterno());
           tboxNac.setValue(persona.getPais());
           tboxFN.setValue(persona.getFechaNacimiento());
           tboxDom.setValue(persona.getCalle());
           tboxCP.setValue(this.vista.campoCP.getText());
           tboxTel.setValue(persona.getTelefono());
           tboxMail.setValue(persona.getEmail().toLowerCase());
           tboxDNI.setValue("DNI: " + persona.getDNI());
           tboxCons.setValue(persona.getConsulado());
           tboxLN.setValue(persona.getCiudadNacimiento() + ", " + persona.getProvinciaNacimiento() + ", " + persona.getPaisNacimiento());
           
           
           LocalDate fechaTurno = LocalDate.parse(persona.getFechaTurno());
           
           DiaFecha.setValue(String.valueOf(fechaTurno.getDayOfMonth()));
           MesFecha.setValue(fechaTurno.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
           AnioFecha.setValue(String.valueOf(fechaTurno.getYear()).substring(1));
           
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
           PDField tboxME = docAcroForm.getField("topmostSubform[0].Page1[0].mun\\.elect[0]");
           PDField tboxPE = docAcroForm.getField("topmostSubform[0].Page1[0].prov\\.elect[0]");
           PDField checkArraigo = docAcroForm.getField("topmostSubform[0].Page1[0].motivo[0]");
           
           
           tboxA1.setValue(persona.getApellidoPaterno());
           tboxA2.setValue(persona.getApellidoMaterno());
           tboxNombres.setValue(persona.getNombres());
           
           tboxDN.setValue(persona.getFechaNacimiento().substring(0, 2));
           tboxMN.setValue(persona.getFechaNacimiento().substring(3,5));
           tboxAN.setValue(persona.getFechaNacimiento().substring(6, 10));
           
           tboxCiudad.setValue(persona.getCiudadNacimiento());
           tboxProv.setValue(persona.getProvinciaNacimiento() + " (" + persona.getPaisNacimiento() + ")");
           tboxConsulado.setValue(persona.getConsulado());
           
           PDRadioButton radio = ((PDRadioButton) checkArraigo);
           radio.setValue("1");
           
           
           
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
           PDField lbox = docAcroForm.getField("lbox1");
           PDField tboxNom = docAcroForm.getField("tBoxNom");
           PDField tboxA1 = docAcroForm.getField("tBoxApe");
           PDField tboxA2 = docAcroForm.getField("tBoxApe2");
           PDField tboxNac = docAcroForm.getField("tBoxNac");
           PDField tboxFijo = docAcroForm.getField("tBoxTel");
           PDField tboxTel = docAcroForm.getField("tBoxMovil");
           PDField tboxMail = docAcroForm.getField("tBoxMail");
           PDField tboxC1 = docAcroForm.getField("Text Box 1_41");
           PDField tboxC2 = docAcroForm.getField("Text Box 1_42");
           PDField tboxDN = docAcroForm.getField("tBoxDomicilio");
           PDField tboxPE = docAcroForm.getField("tBoxMuniEs");
           PDField tboxConsOrig = docAcroForm.getField("tBoxPadronEs");
           PDField checkM = docAcroForm.getField("Check Box 1_2");
           PDField checkF = docAcroForm.getField("Check Box 1");
           PDField checkSoltero = docAcroForm.getField("Check Box 1_3");
           PDField checkCasado = docAcroForm.getField("Check Box 1_4");
           PDField checkSeparado = docAcroForm.getField("Check Box 1_5");
           PDField checkDivor = docAcroForm.getField("Check Box 1_6");
           PDField checkViudo = docAcroForm.getField("Check Box 1_7");
           PDField checkEso = docAcroForm.getField("Check Box 1_9");
           PDField checkBachi = docAcroForm.getField("Check Box 1_10");
           PDField checkUniv = docAcroForm.getField("Check Box 1_11");
           PDField checkNoConsta = docAcroForm.getField("Check Box 1_8");
           PDField domLargo = docAcroForm.getField("tBoxDom2");
           PDField cont1 = docAcroForm.getField("tPriContacto");
           PDField cont2 = docAcroForm.getField("tboxOtroContacto");
           PDField cony = docAcroForm.getField("tBoxConyuge");
           

           //hijo 1
           PDField nh1 = docAcroForm.getField("tBoxH1");
           PDField lnh1 = docAcroForm.getField("tBoxHNac1");
           
           // digitos de nacimiento hijo 1
           PDField dd1h1 = docAcroForm.getField("dd1");
           PDField dd2h1 = docAcroForm.getField("dd2");
           PDField dm1h1 = docAcroForm.getField("dm1");
           PDField dm2h1 = docAcroForm.getField("dm3");
           PDField da1h1 = docAcroForm.getField("da1");
           PDField da2h1 = docAcroForm.getField("da2");
           PDField da3h1 = docAcroForm.getField("da3");
           PDField da4h1 = docAcroForm.getField("da4");
           
           //hijo2
           PDField nh2 = docAcroForm.getField("tBoxH1_2");
           PDField lnh2 = docAcroForm.getField("tBoxHNac1_2");
           
           // digitos de nacimiento hijo 2
           PDField dd1h2 = docAcroForm.getField("dd1h2");
           PDField dd2h2 = docAcroForm.getField("dd2h2");
           PDField dm1h2 = docAcroForm.getField("dm1h2");
           PDField dm2h2 = docAcroForm.getField("dm2h2");
           PDField da1h2 = docAcroForm.getField("da1h2");
           PDField da2h2 = docAcroForm.getField("da2h2");
           PDField da3h2 = docAcroForm.getField("da3h2");
           PDField da4h2 = docAcroForm.getField("da4h2");
           
           //hijo3
           PDField nh3 = docAcroForm.getField("tBoxH1_3");
           PDField lnh3 = docAcroForm.getField("tBoxHNac1_3");
           
           // digitos de nacimiento hijo 3
           PDField dd1h3 = docAcroForm.getField("dd1h3");
           PDField dd2h3 = docAcroForm.getField("dd2h3");
           PDField dm1h3 = docAcroForm.getField("dm1h3");
           PDField dm2h3 = docAcroForm.getField("dm2h3");
           PDField da1h3 = docAcroForm.getField("da1h3");
           PDField da2h3 = docAcroForm.getField("da2h3");
           PDField da3h3 = docAcroForm.getField("da3h3");
           PDField da4h3 = docAcroForm.getField("da4h3");
           
           //hijo4
           PDField nh4 = docAcroForm.getField("tBoxH1_4");
           PDField lnh4 = docAcroForm.getField("tBoxHNac1_4");
           
           // digitos de nacimiento hijo 4
           PDField dd1h4 = docAcroForm.getField("dd1h4");
           PDField dd2h4 = docAcroForm.getField("dd2h4");
           PDField dm1h4 = docAcroForm.getField("dm1h4");
           PDField dm2h4 = docAcroForm.getField("dm2h4");
           PDField da1h4 = docAcroForm.getField("da1h4");
           PDField da2h4 = docAcroForm.getField("da2h4");
           PDField da3h4 = docAcroForm.getField("da3h4");
           PDField da4h4 = docAcroForm.getField("da4h4");
           
           //hijo5
           PDField nh5 = docAcroForm.getField("tBoxH1_5");
           PDField lnh5 = docAcroForm.getField("tBoxHNac1_5");
           
           // digitos de nacimiento hijo 5
           PDField dd1h5 = docAcroForm.getField("dd1h5");
           PDField dd2h5 = docAcroForm.getField("dd2h5");
           PDField dm1h5 = docAcroForm.getField("dm1h5");
           PDField dm2h5 = docAcroForm.getField("dm2h5");
           PDField da1h5 = docAcroForm.getField("da1h5");
           PDField da2h5 = docAcroForm.getField("da2h5");
           PDField da3h5 = docAcroForm.getField("da3h5");
           PDField da4h5 = docAcroForm.getField("da4h5");
           
           
           ((PDComboBox) lbox).setValue("NO");
           tboxNom.setValue(persona.getNombres());
           tboxA1.setValue(persona.getApellidoPaterno());
           tboxA2.setValue(persona.getApellidoMaterno());
           tboxNac.setValue(persona.getFechaNacimiento());
           tboxFijo.setValue("---------------");
           tboxTel.setValue(persona.getTelefono());
           tboxMail.setValue(persona.getEmail());
           tboxC1.setValue(persona.getConsulado());
           tboxC2.setValue(persona.getConsulado());
           tboxDN.setValue(persona.getCiudadNacimiento() + ", " + persona.getProvinciaNacimiento() + ", " + persona.getPaisNacimiento());
           domLargo.setValue(persona.getCalle()+ ", " + this.vista.campoCP.getText() + ", " + persona.getCiudad() + ", " +  persona.getProvincia() + ", " + persona.getPais());
           
           
           if (persona.getSexo().equals("MASCULINO")) {
               ((PDCheckBox) checkM).check();
           } else {
               ((PDCheckBox) checkF).check();
           }
           
           if (persona.getEstadoCivil().equals("SOLTERO")) {
               ((PDCheckBox) checkSoltero).check();
           } else if (persona.getEstadoCivil().equals("CASADO")) {
               ((PDCheckBox) checkCasado).check();
           } else if (persona.getEstadoCivil().equals("SEPARADO")) {
               ((PDCheckBox) checkSeparado).check();
           } else if (persona.getEstadoCivil().equals("DIVORCIADO")) {
               ((PDCheckBox) checkDivor).check();
           } else {
               ((PDCheckBox) checkViudo).check();
           }
           
           if (this.vista.comboEstudios.getSelectedItem().toString().equals("Primaria / Secundaria")) {
               ((PDCheckBox) checkEso).check();
           } else if (this.vista.comboEstudios.getSelectedItem().toString().equals("Bachiller")) {
               ((PDCheckBox) checkBachi).check();
           } else if(this.vista.comboEstudios.getSelectedItem().toString().equals("Universitario")) {
               ((PDCheckBox) checkUniv).check();
           } else {
               ((PDCheckBox) checkNoConsta).check();
           }
           
           tboxPE.setValue(persona.getMunEsp() + "  -  " + persona.getProvEsp());
           tboxConsOrig.setValue("ROSARIO  -  ARGENTINA");
           
           cont1.setValue(this.vista.campoNC.getText().toUpperCase() + ", " + this.vista.campoDC1.getText().toUpperCase() + ", "+ this.vista.campoTC1.getText());
           cont2.setValue(this.vista.campoNC2.getText().toUpperCase() + ", " + this.vista.campoDC2.getText().toUpperCase() + ", "+ this.vista.campoTC2.getText());
           cony.setValue(this.vista.campoCony.getText().toUpperCase());
           
           nh1.setValue(this.vista.campoNH1.getText().toUpperCase());
           lnh1.setValue(this.vista.campoLNH1.getText().toUpperCase());
           
           if (this.vista.campoFNH1.getDate() != null) {
               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
               String fechaN1 = sdf.format(this.vista.campoFNH1.getDate());

               dd1h1.setValue(String.valueOf(fechaN1.charAt(0)));
               dd2h1.setValue(String.valueOf(fechaN1.charAt(1)));
               dm1h1.setValue(String.valueOf(fechaN1.charAt(2)));
               dm2h1.setValue(String.valueOf(fechaN1.charAt(3)));
               da1h1.setValue(String.valueOf(fechaN1.charAt(4)));
               da2h1.setValue(String.valueOf(fechaN1.charAt(5)));
               da3h1.setValue(String.valueOf(fechaN1.charAt(6)));
               da4h1.setValue(String.valueOf(fechaN1.charAt(7)));
           }
           
           nh2.setValue(this.vista.campoNH2.getText().toUpperCase());
           lnh2.setValue(this.vista.campoLNH2.getText().toUpperCase());
           
           if (this.vista.campoFNH2.getDate() != null) {
               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
               String fechaN2 = sdf.format(this.vista.campoFNH2.getDate());

               dd1h2.setValue(String.valueOf(fechaN2.charAt(0)));
               dd2h2.setValue(String.valueOf(fechaN2.charAt(1)));
               dm1h2.setValue(String.valueOf(fechaN2.charAt(2)));
               dm2h2.setValue(String.valueOf(fechaN2.charAt(3)));
               da1h2.setValue(String.valueOf(fechaN2.charAt(4)));
               da2h2.setValue(String.valueOf(fechaN2.charAt(5)));
               da3h2.setValue(String.valueOf(fechaN2.charAt(6)));
               da4h2.setValue(String.valueOf(fechaN2.charAt(7)));
           }
           
           nh3.setValue(this.vista.campoNH3.getText().toUpperCase());
           lnh3.setValue(this.vista.campoLNH3.getText().toUpperCase());
           
           if (this.vista.campoFNH3.getDate() != null) {
               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
               String fechaN3 = sdf.format(this.vista.campoFNH3.getDate());

               dd1h3.setValue(String.valueOf(fechaN3.charAt(0)));
               dd2h3.setValue(String.valueOf(fechaN3.charAt(1)));
               dm1h3.setValue(String.valueOf(fechaN3.charAt(2)));
               dm2h3.setValue(String.valueOf(fechaN3.charAt(3)));
               da1h3.setValue(String.valueOf(fechaN3.charAt(4)));
               da2h3.setValue(String.valueOf(fechaN3.charAt(5)));
               da3h3.setValue(String.valueOf(fechaN3.charAt(6)));
               da4h3.setValue(String.valueOf(fechaN3.charAt(7)));
           }
           
           nh4.setValue(this.vista.campoNH4.getText().toUpperCase());
           lnh4.setValue(this.vista.campoLNH4.getText().toUpperCase());
           
           if (this.vista.campoFNH4.getDate() != null) {
               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
               String fechaN4 = sdf.format(this.vista.campoFNH4.getDate());

               dd1h4.setValue(String.valueOf(fechaN4.charAt(0)));
               dd2h4.setValue(String.valueOf(fechaN4.charAt(1)));
               dm1h4.setValue(String.valueOf(fechaN4.charAt(2)));
               dm2h4.setValue(String.valueOf(fechaN4.charAt(3)));
               da1h4.setValue(String.valueOf(fechaN4.charAt(4)));
               da2h4.setValue(String.valueOf(fechaN4.charAt(5)));
               da3h4.setValue(String.valueOf(fechaN4.charAt(6)));
               da4h4.setValue(String.valueOf(fechaN4.charAt(7)));
           }
           
           nh5.setValue(this.vista.campoNH5.getText().toUpperCase());
           lnh5.setValue(this.vista.campoLNH5.getText().toUpperCase());
           
           if (this.vista.campoFNH5.getDate() != null) {
               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
               String fechaN5 = sdf.format(this.vista.campoFNH5.getDate());

               dd1h5.setValue(String.valueOf(fechaN5.charAt(0)));
               dd2h5.setValue(String.valueOf(fechaN5.charAt(1)));
               dm1h5.setValue(String.valueOf(fechaN5.charAt(2)));
               dm2h5.setValue(String.valueOf(fechaN5.charAt(3)));
               da1h5.setValue(String.valueOf(fechaN5.charAt(4)));
               da2h5.setValue(String.valueOf(fechaN5.charAt(5)));
               da3h5.setValue(String.valueOf(fechaN5.charAt(6)));
               da4h5.setValue(String.valueOf(fechaN5.charAt(7)));
           }
           
           
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
        this.vista.comboEC.setSelectedIndex(0);
        this.vista.combo1.setSelectedIndex(0);
        this.vista.comboSx.setSelectedIndex(0);
        this.vista.getCampoNacio().setText("");
        this.vista.getCampoCalle().setText("");
        this.vista.getCampoCiudad().setText("");
        this.vista.getCampoProvincia().setText("");
        this.vista.getCampoPais().setText("");
        this.vista.getCampoTelefono().setText("");
        this.vista.getCampoEmail().setText("");
        this.vista.getCampoFN().setCalendar(null);
        this.vista.campoFT.setCalendar(null);
        this.vista.getCampoConsulado().setText("");
        this.vista.getCampoCN().setText("");
        this.vista.getCampoPrN().setText("");
        this.vista.getCampoPN().setText("");
        this.vista.campoCP.setText("");
        this.vista.campoHN.setText("");
        this.vista.campoRC.setText("");
        this.vista.campoFI.setCalendar(null);
        this.vista.campoTomo.setText("");
        this.vista.campoPagina.setText("");
        this.vista.campoNroPag.setText("");
        this.vista.campoNC.setText("");
        this.vista.campoDC1.setText("");
        this.vista.campoTC1.setText("");
        this.vista.campoNC2.setText("");
        this.vista.campoDC2.setText("");
        this.vista.campoTC2.setText("");
        this.vista.campoCony.setText("");
        this.vista.campoNH1.setText("");
        this.vista.campoNH2.setText("");
        this.vista.campoNH3.setText("");
        this.vista.campoNH4.setText("");
        this.vista.campoNH5.setText("");
        this.vista.campoLNH1.setText("");
        this.vista.campoLNH2.setText("");
        this.vista.campoLNH3.setText("");
        this.vista.campoLNH4.setText("");
        this.vista.campoLNH5.setText("");
        this.vista.campoFNH1.setDate(null);
        this.vista.campoFNH2.setDate(null);
        this.vista.campoFNH3.setDate(null);
        this.vista.campoFNH4.setDate(null);
        this.vista.campoFNH5.setDate(null);
        
    }
    
    public String numeroADia(int numero) {
        Map<Integer, String> dias = new HashMap<>();
        dias.put(1, "uno");
        dias.put(2, "dos");
        dias.put(3, "tres");
        dias.put(4, "cuatro");
        dias.put(5, "cinco");
        dias.put(6, "seis");
        dias.put(7, "siete");
        dias.put(8, "ocho");
        dias.put(9, "nueve");
        dias.put(10, "diez");
        dias.put(11, "once");
        dias.put(12, "doce");
        dias.put(13, "trece");
        dias.put(14, "catorce");
        dias.put(15, "quince");
        dias.put(16, "dieciséis");
        dias.put(17, "diecisiete");
        dias.put(18, "dieciocho");
        dias.put(19, "diecinueve");
        dias.put(20, "veinte");
        dias.put(21, "veintiuno");
        dias.put(22, "veintidós");
        dias.put(23, "veintitrés");
        dias.put(24, "veinticuatro");
        dias.put(25, "veinticinco");
        dias.put(26, "veintiséis");
        dias.put(27, "veintisiete");
        dias.put(28, "veintiocho");
        dias.put(29, "veintinueve");
        dias.put(30, "treinta");
        dias.put(31, "treinta y uno");

        return dias.getOrDefault(numero, "Número no válido");
    }
}
