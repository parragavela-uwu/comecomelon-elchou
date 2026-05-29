package com.mycompany.elchou;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;
import java.net.URL;

/**
 * Clase principal que inicializa y muestra la interfaz de inicio de sesión.
 * 
 * @author UNFV
 */
public class Elchou {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ComeComelon - Iniciar Sesión");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 640);
            frame.setMinimumSize(new java.awt.Dimension(450, 580));
            frame.setResizable(false);
            
            Image windowIcon = getWindowIconImage();
            if (windowIcon != null) {
                frame.setIconImage(windowIcon);
            }
            
            Ingreso ingresoPanel = new Ingreso();
            frame.add(ingresoPanel);
            
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }


    private static Image getWindowIconImage() {
        URL imgUrl = Elchou.class.getResource("/com/mycompany/elchou/pollito-icon.png");
        if (imgUrl != null) {
            return new ImageIcon(imgUrl).getImage();
        }
        
        File file = new File("pollito-icon.png");
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath()).getImage();
        }
        
        file = new File("src/main/java/com/mycompany/elchou/pollito-icon.png");
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath()).getImage();
        }
        
        return null;
    }
}
