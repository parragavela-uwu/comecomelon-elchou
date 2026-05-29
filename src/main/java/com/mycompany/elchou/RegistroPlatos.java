package com.mycompany.elchou;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class RegistroPlatos extends JFrame {

    private JTextArea txtMarino;
    private JTextArea txtCriollo;
    private JTextArea txtSelvatico;
    private JTextArea txtBienriquito;
    private JTextArea txtPrecioso;

    private JButton btnGuardar;
    private JButton btnVerLista;
    private JButton btnCerrarSesion;

    public RegistroPlatos() {
        super("ComeComelon - Personalizar Categorías");
        initComponents();
        loadExistingData();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(580, 680);
        setMinimumSize(new Dimension(500, 600));
        setLocationRelativeTo(null);
        setResizable(false);

        Image windowIcon = getWindowIconImage();
        if (windowIcon != null) {
            setIconImage(windowIcon);
        }

        setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("ComeComelon");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Personaliza las descripciones de cada tipo de plato");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitle.setBorder(new EmptyBorder(5, 0, 0, 0));

        pnlHeader.add(lblTitle);
        pnlHeader.add(lblSubtitle);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new GridBagLayout());
        pnlForm.setBorder(new EmptyBorder(15, 25, 15, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 0);

        txtMarino = createStyledTextAreaField(pnlForm, "Categoría Marino", gbc);
        txtCriollo = createStyledTextAreaField(pnlForm, "Categoría Criolla", gbc);
        txtSelvatico = createStyledTextAreaField(pnlForm, "Categoría Selvática", gbc);
        txtBienriquito = createStyledTextAreaField(pnlForm, "Categoría Bienriquito (Carretilla & Sazón)", gbc);
        txtPrecioso = createStyledTextAreaField(pnlForm, "Categoría Preciosa (Gourmet & Fusión)", gbc);

        JScrollPane scrollPane = new JScrollPane(pnlForm);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel();
        pnlButtons.setBorder(new EmptyBorder(15, 25, 15, 25));
        pnlButtons.setLayout(new GridLayout(1, 3, 15, 0));

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> logout());

        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(e -> saveDescriptions(false));

        btnVerLista = new JButton("Ver Carta de Platos ->");
        btnVerLista.addActionListener(e -> {
            saveDescriptions(true);
            openListaPlatos();
        });

        pnlButtons.add(btnCerrarSesion);
        pnlButtons.add(btnGuardar);
        pnlButtons.add(btnVerLista);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    private JTextArea createStyledTextAreaField(JPanel parent, String labelTitle, GridBagConstraints gbc) {
        JPanel container = new JPanel(new BorderLayout(0, 5));

        JLabel label = new JLabel(labelTitle);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        container.add(label, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(2, 40);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane textScroll = new JScrollPane(textArea);
        container.add(textScroll, BorderLayout.CENTER);

        parent.add(container, gbc);
        return textArea;
    }

    private void loadExistingData() {
        txtMarino.setText(ListaPlatos.descripcionesCategorias.get("Marino"));
        txtCriollo.setText(ListaPlatos.descripcionesCategorias.get("Criollo"));
        txtSelvatico.setText(ListaPlatos.descripcionesCategorias.get("Selvático"));
        txtBienriquito.setText(ListaPlatos.descripcionesCategorias.get("Bienriquito"));
        txtPrecioso.setText(ListaPlatos.descripcionesCategorias.get("Precioso"));
    }

    private void saveDescriptions(boolean silent) {
        ListaPlatos.descripcionesCategorias.put("Marino", txtMarino.getText().trim());
        ListaPlatos.descripcionesCategorias.put("Criollo", txtCriollo.getText().trim());
        ListaPlatos.descripcionesCategorias.put("Selvático", txtSelvatico.getText().trim());
        ListaPlatos.descripcionesCategorias.put("Bienriquito", txtBienriquito.getText().trim());
        ListaPlatos.descripcionesCategorias.put("Precioso", txtPrecioso.getText().trim());

        if (!silent) {
            JOptionPane.showMessageDialog(this, 
                "¡Descripciones de categorías guardadas con éxito!", 
                "Información Guardada", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openListaPlatos() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            ListaPlatos frame3 = new ListaPlatos();
            frame3.setVisible(true);
        });
    }

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ComeComelon - Iniciar Sesión");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 640);
            frame.setMinimumSize(new Dimension(450, 580));
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

    private Image getWindowIconImage() {
        URL imgUrl = RegistroPlatos.class.getResource("/com/mycompany/elchou/pollito-icon.png");
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
