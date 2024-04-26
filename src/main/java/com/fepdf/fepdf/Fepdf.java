package com.fepdf.fepdf;

import com.fepdf.fepdf.controlador.ControllerMainFrame;
import com.fepdf.fepdf.vista.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

public class Fepdf {

    public static void main(String[] args) {
        FlatLightLaf.setup();
        MainFrame fr = new MainFrame();
        ControllerMainFrame controller = new ControllerMainFrame(fr);
        
        fr.setVisible(true);
    }
}
