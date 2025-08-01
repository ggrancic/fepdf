package com.fepdf.fepdf.controlador;

import com.fepdf.fepdf.modelo.Persona;
import com.fepdf.fepdf.vista.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
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
        persona.setNacionalidad(this.vista.getCampoNacio().getText().toUpperCase());
        persona.setDNI(this.vista.getCampoDNI().getText().toUpperCase());
        persona.setCalle(this.vista.getCampoCalle().getText().toUpperCase());
        persona.setAltura(this.vista.getCampoAltura().getText().toUpperCase());
        persona.setPiso(this.vista.getCampoPiso().getText().toUpperCase());
        
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
        
        
        if ((this.vista.comboSx.getSelectedItem().toString()).equals("Femenino")) {
            
            String sourceString = this.vista.comboEC.getSelectedItem().toString().toUpperCase();
            int lastChar = sourceString.length() - 1;
            
            StringBuilder sb = new StringBuilder(sourceString);
            sb.setCharAt(lastChar, 'A');
            
            persona.setEstadoCivil(sb.toString());
        } else {
            persona.setEstadoCivil(this.vista.comboEC.getSelectedItem().toString().toUpperCase());
        }
        
        
        
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
           
           if (!(persona.getAltura().isBlank())) {
               if (!(persona.getPiso().isBlank())) {
                   tboxDom.setValue(persona.getCalle() + " " + persona.getAltura() + " PISO " + persona.getPiso());
               } else {
                   tboxDom.setValue(persona.getCalle() + " " + persona.getAltura());
               }
           } else {
               tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad());
           }
           
           
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
           PDField tboxDiaTurno = docAcroForm.getField("FirmaDIA");
           PDField tboxMesTurno = docAcroForm.getField("FirmaMES");
           PDField tboxAgnoTurno = docAcroForm.getField("FirmaAÑO");
           
           
           tboxRC.setValue(persona.getConsulado());
           tboxNombres.setValue(persona.getNombres());
           tboxAM.setValue(persona.getApellidoMaterno());
           tboxAP.setValue(persona.getApellidoPaterno());
           tboxNac.setValue(persona.getNacionalidad());
           tboxEC.setValue(persona.getEstadoCivil());
           tboxDNI.setValue("DNI: " + persona.getDNI());
           
           if (!(persona.getAltura().isBlank())) {
               if (!(persona.getPiso().isBlank())) {
                   tboxDom.setValue(persona.getCalle() + " " + persona.getAltura() + " PISO " + persona.getPiso());
               } else {
                   tboxDom.setValue(persona.getCalle() + " " + persona.getAltura());
               }
           } else {
               tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad());
           }
           
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
           
           LocalDate fechaTurno = LocalDate.parse(persona.getFechaTurno());
           
           
           tboxDiaTurno.setValue(fechaTurno.getDayOfMonth() + "");
           tboxMesTurno.setValue(fechaTurno.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
           tboxAgnoTurno.setValue(fechaTurno.getYear() + "");
           
           ((PDCheckBox) checkComun).check();
           ((PDCheckBox) check1).check();
           ((PDCheckBox) check2).check();
           ((PDCheckBox) check3).check();
           ((PDCheckBox) check4).check();
           
           if (this.vista.combo1.getSelectedItem().toString().equals("SI")) {
               tboxOtros.setValue("ACTA DE MATRIMONIO DE LOS PROGENITORES");
           } else {
               tboxOtros.setValue("ACTA DE NACIMIENTO DEL PROGENITOR NO ESPAÑOL");
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
           
           if (!(persona.getAltura().isBlank())) {
               if (!(persona.getPiso().isBlank())) {
                   tboxDom.setValue(persona.getCalle() + " " + persona.getAltura() + " PISO " + persona.getPiso());
               } else {
                   tboxDom.setValue(persona.getCalle() + " " + persona.getAltura());
               }
           } else {
               tboxDom.setValue(persona.getCalle() + ", " + persona.getCiudad());
           }
           
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
           PDField nombrePadre = docAcroForm.getField("NombreProgA");
           PDField apellido1Padre = docAcroForm.getField("Apellido1ProgA");
           PDField apellido2Padre = docAcroForm.getField("Apellido2ProgA");
           PDField pp = docAcroForm.getField("Progenitor1ProgA");
           PDField mp = docAcroForm.getField("Progenitor2ProgA");
           PDField lnp = docAcroForm.getField("LugarNacProgA");
           PDField dnp = docAcroForm.getField("DiaNacProgA");
           PDField mnp = docAcroForm.getField("MesNacProgA");
           PDField anp = docAcroForm.getField("AnioNacProgA");
           PDField ecnp = docAcroForm.getField("EstadoCivilAlNacProg1");
           PDField ecap = docAcroForm.getField("EstadoCivilActual");
           PDField nnp = docAcroForm.getField("NacionalidadAlNacProgA");
           PDField nap = docAcroForm.getField("NacionalidadActualProgA");
           PDField domp = docAcroForm.getField("DomicilioProgA");
           
           PDField nombreMadre = docAcroForm.getField("NombreProgB");
           PDField apellido1Madre = docAcroForm.getField("Apellido1ProgB");
           PDField apellido2Madre = docAcroForm.getField("Apellido2ProgB");
           PDField pm = docAcroForm.getField("Progenitor1ProgB");
           PDField mm = docAcroForm.getField("Progenitor2ProgB");
           PDField lnm = docAcroForm.getField("LugarNacProgB");
           PDField dnm = docAcroForm.getField("DiaNacProgB");
           PDField mnm = docAcroForm.getField("MesNacProgB");
           PDField anm = docAcroForm.getField("AnioNacProgB");
           PDField ecnm = docAcroForm.getField("EstadoCivilAlNacProgB");
           PDField ecam = docAcroForm.getField("EstadoCivilActualProgB");
           PDField nnm = docAcroForm.getField("NacionalidadAlNacProgB");
           PDField nam = docAcroForm.getField("NacionalidadActualProgB");
           PDField domm = docAcroForm.getField("DomicilioProgB");
           
           PDField existeMatri = docAcroForm.getField("ExisteMatrimonio");
           PDField diaMatri = docAcroForm.getField("DiaMatrimonio");
           PDField mesMatri = docAcroForm.getField("MesMatrimonio");
           PDField anioMatri = docAcroForm.getField("AnioMatrimonio");
           PDField lugarCeleb = docAcroForm.getField("LugarMatrimonio");
           PDField inscrito = docAcroForm.getField("LugarInscripMatrimonio");
           PDField docuMatri = docAcroForm.getField("DocumentoMatrimonio");
           
           
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
           SimpleDateFormat dfes = new SimpleDateFormat("yyyy-MM-dd");
           
           fechaInsc.setValue(df.format(this.vista.campoFI.getDate()));
           registroCivil.setValue(this.vista.campoRC.getText().toUpperCase());
           tomo.setValue(this.vista.campoTomo.getText());
           pagina.setValue(this.vista.campoPagina.getText());
           numero.setValue(this.vista.campoNroPag.getText());
           
           nombrePadre.setValue(this.vista.campoNomP.getText().toUpperCase());
           apellido1Padre.setValue(this.vista.campoApePadre1.getText().toUpperCase());
           apellido2Padre.setValue(this.vista.campoApePadre2.getText().toUpperCase());
           pp.setValue(this.vista.campoPP.getText().toUpperCase());
           mp.setValue(this.vista.campoMP.getText().toUpperCase());
           lnp.setValue(this.vista.campoNacP.getText().toUpperCase());
           nnp.setValue(this.vista.nacioNacP.getText().toUpperCase());
           nap.setValue(this.vista.nacioAcP.getText().toUpperCase());
           domp.setValue(this.vista.domP.getText().toUpperCase());
           
           LocalDate fechaPadre = LocalDate.parse(dfes.format(this.vista.campoFNP.getDate()));
           dnp.setValue(String.valueOf(fechaPadre.getDayOfMonth()));
           mnp.setValue(fechaPadre.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
           anp.setValue(String.valueOf(fechaPadre.getYear()));
           ecnp.setValue(this.vista.comboEcNacP.getSelectedItem().toString().toUpperCase());
           ecap.setValue(this.vista.comboEcAcP.getSelectedItem().toString().toUpperCase());
           
           nombreMadre.setValue(this.vista.campoNomM.getText().toUpperCase());
           apellido1Madre.setValue(this.vista.campoApeMadre.getText().toUpperCase());
           apellido2Madre.setValue(this.vista.campoApeMadre2.getText().toUpperCase());
           pm.setValue(this.vista.campoPM.getText().toUpperCase());
           mm.setValue(this.vista.campoMM.getText().toUpperCase());
           lnm.setValue(this.vista.campoNacM.getText().toUpperCase());
           nnm.setValue(this.vista.nacioNacM.getText().toUpperCase());
           nam.setValue(this.vista.nacioAcM.getText().toUpperCase());
           domm.setValue(this.vista.domP.getText().toUpperCase());
           
           LocalDate fechaMadre = LocalDate.parse(dfes.format(this.vista.campoFNM.getDate()));
           dnm.setValue(String.valueOf(fechaMadre.getDayOfMonth()));
           mnm.setValue(fechaMadre.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
           anm.setValue(String.valueOf(fechaMadre.getYear()));
           ecnm.setValue(this.vista.comboEcNacM.getSelectedItem().toString().toUpperCase());
           ecam.setValue(this.vista.comboEcAcM.getSelectedItem().toString().toUpperCase());
           
           tboxDeclarante.setValue(persona.getNombres() + " " + persona.getApellidoPaterno());
           tboxNac.setValue(persona.getPais());
           tboxFN.setValue(persona.getFechaNacimiento());
           
           if (!(persona.getAltura().isBlank())) {
               if (!(persona.getPiso().isBlank())) {
                   tboxDom.setValue(persona.getCalle() + " " + persona.getAltura() + " PISO " + persona.getPiso());
               } else {
                   tboxDom.setValue(persona.getCalle() + " " + persona.getAltura());
               }
           } else {
               tboxDom.setValue(persona.getCalle());
           }
           
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
           
           if (this.vista.combo1.getSelectedItem().toString().equals("SI")) {
               existeMatri.setValue("EXISTE");
               LocalDate fechaMatri = LocalDate.parse(dfes.format(this.vista.fechaMatri.getDate()));
               diaMatri.setValue(this.numeroADia(fechaMatri.getDayOfMonth()).toUpperCase());
               mesMatri.setValue(fechaMatri.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
               anioMatri.setValue(String.valueOf(fechaMatri.getYear()));
               lugarCeleb.setValue(this.vista.campoLugar.getText().toUpperCase());
               inscrito.setValue(this.vista.inscripcion.getText().toUpperCase());
               docuMatri.setValue("ACTA DE MATRIMONIO");
           } else {
               existeMatri.setValue("NO EXISTE");
           }
           
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
           PDPage page = docDecl.getPage(0);
           PDPageContentStream cstream = new PDPageContentStream(docDecl, page, PDPageContentStream.AppendMode.PREPEND, true, true);
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
           
           tboxME.setValue(persona.getMunEsp().toUpperCase());
           tboxPE.setValue(persona.getProvEsp().toUpperCase());
           
           tboxDN.setValue(persona.getFechaNacimiento().substring(0, 2));
           tboxMN.setValue(persona.getFechaNacimiento().substring(3,5));
           tboxAN.setValue(persona.getFechaNacimiento().substring(6, 10));
           
           tboxCiudad.setValue(persona.getCiudadNacimiento());
           tboxProv.setValue(persona.getProvinciaNacimiento() + " (" + persona.getPaisNacimiento() + ")");
           tboxConsulado.setValue("C.G. " + persona.getConsulado());
           
           PDRadioButton radio = ((PDRadioButton) checkArraigo);
           radio.setValue("1");
           
           
           cstream.beginText();
           cstream.newLineAtOffset(210, 167);
           cstream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
           cstream.showText(persona.getConsulado().substring(0,1).toUpperCase() + persona.getConsulado().substring(1).toLowerCase());
           cstream.endText();
           cstream.close();
           
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
           byte[] byteArray = IOUtils.toByteArray((this.getClass().getResourceAsStream("/pdf/solicitudInscripcionResidenteEditable2.pdf")));       
           PDDocument docSoli = Loader.loadPDF(byteArray);
           PDAcroForm docAcroForm = docSoli.getDocumentCatalog().getAcroForm();
           
           PDField tboxConsulado = docAcroForm.getField("tboxConsulado");
           PDField tboxNom = docAcroForm.getField("tBoxNom");
           PDField tboxA1 = docAcroForm.getField("tBoxApe");
           PDField tboxA2 = docAcroForm.getField("tBoxApe2");
           PDField tboxNac = docAcroForm.getField("tBoxNac");
           PDField tboxDNI = docAcroForm.getField("tBoxDNI");
           
           PDField tboxPaisNac = docAcroForm.getField("tBoxPaisNac");
           PDField tboxProvNac = docAcroForm.getField("tboxProvNac");
           PDField tboxMuniNac = docAcroForm.getField("tboxMuniNac");
           
           PDField tBoxDomicilio = docAcroForm.getField("tBoxDomiciliio");
           PDField tBoxNroDom = docAcroForm.getField("tBoxNroDom");
           PDField tBoxPlanta = docAcroForm.getField("tBoxPlanta");
           
           PDField tboxCP = docAcroForm.getField("tBoxCP");
           PDField tboxLocalidad = docAcroForm.getField("tBoxLocalidad");
           PDField tboxPais = docAcroForm.getField("tBoxPais");
           
           
           PDField checkH = docAcroForm.getField("checkHombre");
           PDField checkM = docAcroForm.getField("checkMujer");
           
           PDField checkInferior = docAcroForm.getField("checkNoPrimaria");
           PDField checkEscolar = docAcroForm.getField("checkPrimaria");
           PDField checkESOSuperior = docAcroForm.getField("checkESOSuperior");
           PDField checkNoConsta = docAcroForm.getField("checkNoConsta");
           
           PDField tboxFijo = docAcroForm.getField("tBoxTel");
           PDField tboxTel = docAcroForm.getField("tBoxMovil");
           PDField tboxMail = docAcroForm.getField("tBoxMail");
           PDField tboxC1 = docAcroForm.getField("Text Box 1_41");
           PDField tboxC2 = docAcroForm.getField("Text Box 1_42");
           
           PDField tboxEmpadrona = docAcroForm.getField("Empadrona");
           PDField tboxMuniEsp = docAcroForm.getField("MunInscrip");
           PDField tboxProvEsp = docAcroForm.getField("ProvInscrip");
           PDField checkSoltero = docAcroForm.getField("checkSoltero");
           PDField checkCasado = docAcroForm.getField("checkCasado");
           PDField checkSeparado = docAcroForm.getField("checkSeparado");
           PDField checkDivor = docAcroForm.getField("checkDivor");
           PDField checkViudo = docAcroForm.getField("checkViudo");
           
           PDField tBoxNC1 = docAcroForm.getField("tBoxNC1");
           PDField tBoxTC1 = docAcroForm.getField("tBoxTC1");
           PDField tBoxEC1 = docAcroForm.getField("tBoxEC1");
           PDField tBoxDC1 = docAcroForm.getField("tBoxDC1");
           
           PDField tBoxNC2 = docAcroForm.getField("tBoxNC2");
           PDField tBoxTC2 = docAcroForm.getField("tBoxTC2");
           PDField tBoxEC2 = docAcroForm.getField("tBoxEC2");
           PDField tBoxDC2 = docAcroForm.getField("tBoxDC2");
           
           PDField tBoxConyuge = docAcroForm.getField("tBoxConyuge");
           PDField tBoxProgenitores = docAcroForm.getField("tBoxProgenitores");
           

           //hijo 1
           PDField nh1 = docAcroForm.getField("tBoxH1");
           PDField lnh1 = docAcroForm.getField("tBoxHNac1");
           
           // digitos de nacimiento hijo 1
           PDField dd1h1 = docAcroForm.getField("dd1");
           PDField dd2h1 = docAcroForm.getField("dd2");
           PDField dm1h1 = docAcroForm.getField("dm1");
           PDField dm2h1 = docAcroForm.getField("dm2");
           PDField da1h1 = docAcroForm.getField("da1");
           PDField da2h1 = docAcroForm.getField("da2");
           PDField da3h1 = docAcroForm.getField("da3");
           PDField da4h1 = docAcroForm.getField("da4");
           
           //hijo2
           PDField nh2 = docAcroForm.getField("tBoxH2");
           PDField lnh2 = docAcroForm.getField("tBoxHNac2");
           
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
           PDField nh3 = docAcroForm.getField("tBoxH3");
           PDField lnh3 = docAcroForm.getField("tBoxHNac3");
           
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
           PDField nh4 = docAcroForm.getField("tBoxH4");
           PDField lnh4 = docAcroForm.getField("tBoxHNac4");
           
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
           PDField nh5 = docAcroForm.getField("tBoxH5");
           PDField lnh5 = docAcroForm.getField("tBoxHNac5");
           
           // digitos de nacimiento hijo 5
           PDField dd1h5 = docAcroForm.getField("dd1h5");
           PDField dd2h5 = docAcroForm.getField("dd2h5");
           PDField dm1h5 = docAcroForm.getField("dm1h5");
           PDField dm2h5 = docAcroForm.getField("dm2h5");
           PDField da1h5 = docAcroForm.getField("da1h5");
           PDField da2h5 = docAcroForm.getField("da2h5");
           PDField da3h5 = docAcroForm.getField("da3h5");
           PDField da4h5 = docAcroForm.getField("da4h5");
           
           
           tboxConsulado.setValue(persona.getConsulado());
           tboxNom.setValue(persona.getNombres());
           tboxA1.setValue(persona.getApellidoPaterno());
           tboxA2.setValue(persona.getApellidoMaterno());
           tboxNac.setValue(persona.getFechaNacimiento());
           tboxDNI.setValue(persona.getDNI());
           tboxPaisNac.setValue(persona.getPaisNacimiento());
           tboxProvNac.setValue(persona.getProvinciaNacimiento());
           tboxMuniNac.setValue(persona.getCiudadNacimiento());
           tBoxDomicilio.setValue(persona.getCalle());
           tBoxNroDom.setValue(persona.getAltura());
           tBoxPlanta.setValue(persona.getPiso());
           tboxCP.setValue(this.vista.campoCP.getText().trim());
           tboxLocalidad.setValue(persona.getCiudad() + " , "  + persona.getProvincia());
           tboxPais.setValue(persona.getPais());
           tboxEmpadrona.setValue("C.G. " + persona.getConsulado() + " - ARGENTINA");
           tboxMuniEsp.setValue(persona.getMunEsp());
           tboxProvEsp.setValue(persona.getProvEsp());
           
           tboxFijo.setValue("---------------");
           tboxTel.setValue(persona.getTelefono());
           tboxMail.setValue(persona.getEmail());
           tboxC1.setValue(persona.getConsulado());
           tboxC2.setValue(persona.getConsulado());
           
           //domLargo.setValue(persona.getCalle()+ ", " + this.vista.campoCP.getText() + ", " + persona.getCiudad() + ", " +  persona.getProvincia() + ", " + persona.getPais());
           
           
           if (persona.getSexo().equals("MASCULINO")) {
               ((PDCheckBox) checkH).check();
           } else {
               ((PDCheckBox) checkM).check();
           }
           
           if (persona.getEstadoCivil().equals("SOLTERO") || persona.getEstadoCivil().equals("SOLTERA")) {
               ((PDCheckBox) checkSoltero).check();
           } else if (persona.getEstadoCivil().equals("CASADO") || persona.getEstadoCivil().equals("CASADA")) {
               ((PDCheckBox) checkCasado).check();
           } else if (persona.getEstadoCivil().equals("SEPARADO") || persona.getEstadoCivil().equals("SEPARADA")) {
               ((PDCheckBox) checkSeparado).check();
           } else if (persona.getEstadoCivil().equals("DIVORCIADO") || persona.getEstadoCivil().equals("DIVORCIADA")) {
               ((PDCheckBox) checkDivor).check();
           } else {
               ((PDCheckBox) checkViudo).check();
           }
           
           if (this.vista.comboEstudios.getSelectedItem().toString().equals("Primario no terminado")) {
               ((PDCheckBox) checkInferior).check();
           } else if (this.vista.comboEstudios.getSelectedItem().toString().equals("Primario terminado")) {
               ((PDCheckBox) checkEscolar).check();
           } else if(this.vista.comboEstudios.getSelectedItem().toString().equals("Secundario - Terciario - Universitario")) {
               ((PDCheckBox) checkESOSuperior).check();
           } else {
               ((PDCheckBox) checkNoConsta).check();
           }
           
           
           tBoxNC1.setValue(this.vista.campoNC.getText().toUpperCase());
           tBoxDC1.setValue(this.vista.campoDC1.getText().toUpperCase());
           tBoxTC1.setValue(this.vista.campoTC1.getText());
           tBoxEC1.setValue(this.vista.campoCorreoC1.getText());
           
           tBoxNC2.setValue(this.vista.campoNC2.getText().toUpperCase());
           tBoxDC2.setValue(this.vista.campoDC2.getText().toUpperCase());
           tBoxTC2.setValue(this.vista.campoTC2.getText());
           tBoxEC2.setValue(this.vista.campoCorreoC2.getText());
           
           tBoxConyuge.setValue(this.vista.campoCony.getText().toUpperCase());
           
           tBoxProgenitores.setValue(this.vista.getCampoProgenitores().getText().toUpperCase());
           
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
        this.vista.getCampoAltura().setText("");
        this.vista.getCampoPiso().setText("");
        this.vista.getCampoCiudad().setText("");
        this.vista.getCampoProvincia().setText("");
        this.vista.getCampoPais().setText("");
        this.vista.getCampoTelefono().setText("+54 9");
        this.vista.getCampoEmail().setText("");
        this.vista.getCampoFN().setCalendar(null);
        this.vista.campoFT.setCalendar(null);
        this.vista.getCampoConsulado().setText("");
        this.vista.getCampoCN().setText("");
        this.vista.getCampoPrN().setText("");
        this.vista.getCampoPN().setText("");
        this.vista.campoCP.setText("");
        this.vista.campoHN.setText("00:00");
        this.vista.campoRC.setText("");
        this.vista.campoFI.setCalendar(null);
        this.vista.campoTomo.setText("");
        this.vista.campoPagina.setText("");
        this.vista.campoNroPag.setText("");
        this.vista.campoCony.setText("");
        this.vista.campoProgenitores.setText("");
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
        this.vista.campoTC1.setText("+54 9");
        this.vista.campoTC2.setText("+54 9");
        this.vista.campoCorreoC1.setText("");
        this.vista.campoCorreoC2.setText("");
        
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
