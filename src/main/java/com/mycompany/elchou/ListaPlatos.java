package com.mycompany.elchou;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;

public class ListaPlatos extends JFrame {

    private JPanel pnlSidebar;
    private JPanel pnlContent;
    private JScrollPane scrollContent;
    private JLabel lblCategoryTitle;
    private JTextArea txtCategoryDesc;
    private JPanel pnlDishesList;

    private JButton btnVolver;
    private JButton btnCerrarSesion;
    
    private JButton[] categoryButtons;
    private final String[] categories = {"Marino", "Criollo", "Selvático", "Bienriquito", "Precioso"};
    private final String[] days = {
        "Martes (Marino)", 
        "Miércoles (Criollo)", 
        "Jueves (Selvático)", 
        "Viernes (Bienriquito)", 
        "Sábado (Comidita rica)"
    };
    private String activeCategory = "Marino";
    private String activeDayLabel = "Martes (Marino)";
    private boolean isAdmin = true;

    public ListaPlatos() {
        this(true);
    }

    public ListaPlatos(boolean isAdmin) {
        super("ComeComelon - Carta de la semana :v");
        this.isAdmin = isAdmin;
        initComponents();
        selectCategory("Marino");
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 720);
        setMinimumSize(new Dimension(680, 600));
        setLocationRelativeTo(null);
        setResizable(false);

        Image windowIcon = getWindowIconImage();
        if (windowIcon != null) {
            setIconImage(windowIcon);
        }

        setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("ComeComelon - Carta de la semana :v");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        add(pnlHeader, BorderLayout.NORTH);

        pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 12, 8, 12);

        JLabel lblMenu = new JLabel("Menú por Días");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblMenu.setBorder(new EmptyBorder(10, 5, 10, 5));
        pnlSidebar.add(lblMenu, gbc);

        categoryButtons = new JButton[categories.length];
        for (int i = 0; i < categories.length; i++) {
            final String catName = categories[i];
            final String dayName = days[i];
            JButton btn = new JButton(dayName);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.addActionListener(e -> selectCategory(catName));
            categoryButtons[i] = btn;
            pnlSidebar.add(btn, gbc);
        }

        gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        pnlSidebar.add(filler, gbc);

        pnlSidebar.setPreferredSize(new Dimension(200, 0));
        add(pnlSidebar, BorderLayout.WEST);

        pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlCatInfo = new JPanel(new BorderLayout(0, 8));
        pnlCatInfo.setOpaque(false);
        pnlCatInfo.setBorder(new EmptyBorder(0, 0, 15, 0));

        lblCategoryTitle = new JLabel("Comida Marina");
        lblCategoryTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pnlCatInfo.add(lblCategoryTitle, BorderLayout.NORTH);

        pnlContent.add(pnlCatInfo, BorderLayout.NORTH);

        pnlDishesList = new JPanel();
        pnlDishesList.setLayout(new BoxLayout(pnlDishesList, BoxLayout.Y_AXIS));

        scrollContent = new JScrollPane(pnlDishesList);
        pnlContent.add(scrollContent, BorderLayout.CENTER);

        add(pnlContent, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(new EmptyBorder(12, 20, 12, 20));

        btnVolver = new JButton("<- Editar Categorías");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.addActionListener(e -> goBackToRegistro());
        
        if (isAdmin) {
            pnlFooter.add(btnVolver, BorderLayout.WEST);
        } else {
            JButton btnAsistir = new JButton("Reservar");
            btnAsistir.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnAsistir.addActionListener(e -> {
                JOptionPane.showMessageDialog(this,
                    "Reservaste para " + activeDayLabel,
                    "Confirmación de Reserva",
                    JOptionPane.INFORMATION_MESSAGE);
            });
            pnlFooter.add(btnAsistir, BorderLayout.WEST);
        }

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrarSesion.addActionListener(e -> logout());
        pnlFooter.add(btnCerrarSesion, BorderLayout.EAST);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void selectCategory(String category) {
        activeCategory = category;

        int index = -1;
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                index = i;
                break;
            }
        }

        String dayLabel = (index != -1) ? days[index] : category;
        activeDayLabel = dayLabel;
        lblCategoryTitle.setText("Menú del " + dayLabel);
        
        pnlDishesList.removeAll();
        List<Plato> platos = platosPorCategoria.get(category);
        
        if (platos != null && !platos.isEmpty()) {
            for (Plato plato : platos) {
                pnlDishesList.add(createDishCard(plato));
                pnlDishesList.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        } else {
            JLabel lblEmpty = new JLabel("No hay platos disponibles en esta categoría.");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblEmpty.setBorder(new EmptyBorder(20, 10, 0, 0));
            pnlDishesList.add(lblEmpty);
        }

        pnlDishesList.revalidate();
        pnlDishesList.repaint();
        scrollContent.revalidate();
        scrollContent.repaint();
    }

    private JPanel createDishCard(Plato plato) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBorder(new EmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel pnlLeft = new JPanel(new BorderLayout(0, 4));
        pnlLeft.setOpaque(false);

        JLabel lblName = new JLabel(plato.getNombre());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlLeft.add(lblName, BorderLayout.NORTH);

        card.add(pnlLeft, BorderLayout.CENTER);

        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setOpaque(false);

        JLabel lblPrice = new JLabel(plato.getPrecio());
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPrice.setBorder(new EmptyBorder(6, 12, 6, 12));
        
        pnlRight.add(lblPrice);
        card.add(pnlRight, BorderLayout.EAST);

        return card;
    }

    private void goBackToRegistro() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            RegistroPlatos frame2 = new RegistroPlatos();
            frame2.setVisible(true);
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
        URL imgUrl = ListaPlatos.class.getResource("/com/mycompany/elchou/pollito-icon.png");
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

    public static final java.util.Map<String, String> descripcionesCategorias = new java.util.HashMap<>();
    public static final java.util.Map<String, List<Plato>> platosPorCategoria = new java.util.HashMap<>();

    static {
        descripcionesCategorias.put("Marino", "Platos frescos del litoral, pescados y mariscos de alta calidad maridados al instante.");
        descripcionesCategorias.put("Criollo", "Guisos tradicionales y salteados llenos de historia, ajíes y la sazón de la abuela.");
        descripcionesCategorias.put("Selvático", "Sabores rústicos y ahumados de la amazonía con ingredientes nativos de la selva.");
        descripcionesCategorias.put("Bienriquito", "Combinaciones contundentes de carretilla, generosos y llenos de sabor popular.");
        descripcionesCategorias.put("Precioso", "Fusión gourmet con presentación elegante, ingredientes seleccionados y toques artísticos.");

        platosPorCategoria.put("Marino", new java.util.ArrayList<>(java.util.Arrays.asList(
            new Plato("Ceviche Mixto Trujillano", "Fresco lenguado, calamar y conchas negras marinados en limón de Chulucanas y ají limo, con camote glaseado.", "S/. 38.00"),
            new Plato("Tiradito de Pescado al Ají Amarillo", "Finas láminas de lenguado en crema sedosa de ají amarillo, choclo desgranado y hojas de culantro.", "S/. 34.00"),
            new Plato("Arroz con Mariscos a la Limeña", "Arroz sazonado con coral de camarón, pulpo, calamar, langostinos, pimiento y un toque de vino blanco.", "S/. 42.00"),
            new Plato("Jalea Mixta Rabiosa", "Trozos de pescado y mariscos crujientes cubiertos con sarza criolla abundante, yuca frita y crema de rocoto.", "S/. 39.00"),
            new Plato("Parihuela Levanta-Muertos", "Concentrado caldo de mariscos y pescado aromatizado con chicha de jora y culantro, servido caliente.", "S/. 45.00"),
            new Plato("Leche de Tigre con Chicharrón de Pota", "Zumo de ceviche concentrado y picante con trozos de pescado fresco, coronado con chicharrón crujiente de pota.", "S/. 18.00")
        )));
        
        platosPorCategoria.put("Criollo", new java.util.ArrayList<>(java.util.Arrays.asList(
            new Plato("Lomo Saltado Ahumado", "Jugoso lomo de res salteado al wok a fuego alto con cebollas, tomates, ají amarillo, servido con papas amarillas fritas y arroz blanco.", "S/. 46.00"),
            new Plato("Ají de Gallina Cremoso", "Pechuga de gallina deshilachada en crema de ají amarillo, nueces pecanas, queso parmesano y leche evaporada.", "S/. 32.00"),
            new Plato("Anticuchos de Corazón", "Corazón de res macerado en ají panca, ajo y cerveza negra, ensartado y cocinado al carbón, servido con papa dorada y choclo.", "S/. 28.00"),
            new Plato("Seco de Cabrito con Frijoles", "Cabrito tierno guisado en cerveza de jora y culantro fresco, servido con frijoles cremosos y arroz con choclo.", "S/. 48.00"),
            new Plato("Carapulcra con Sopa Seca Chinchana", "Papa seca tostada y guisada con carne de chancho, servida con tallarines aderezados en albahaca y achiote.", "S/. 35.00"),
            new Plato("Causa Rellena de Pollo e Hilos de Papa", "Puré de papa amarilla sazonado con ají amarillo y limón, relleno de pollo deshilachado y palta fuerte, decorado con huevo duro.", "S/. 25.00")
        )));
        
        platosPorCategoria.put("Selvático", new java.util.ArrayList<>(java.util.Arrays.asList(
            new Plato("Juane de Gallina Especial", "Arroz aromático al palillo con presas de gallina, huevo cocido y aceitunas, envuelto y cocinado en hojas de bijao.", "S/. 30.00"),
            new Plato("Tacacho con Cecina y Chorizo", "Plátanos verdes machacados con manteca y chicharrón, acompañados de cecina ahumada de Tarapoto y chorizo amazónico frito.", "S/. 35.00"),
            new Plato("Patarashca de Doncella", "Pescado fresco sazonado con sacha culantro, ají dulce y cebolla, envuelto en hojas de bijao y asado al carbón.", "S/. 38.00"),
            new Plato("Ensalada de Chonta del Oriente", "Tallos de palmito cortados en finos hilos aderezados con limón, aceite de oliva, cebolla roja y ají de cocona.", "S/. 22.00"),
            new Plato("Caldo de Carachama Energizante", "Sustancioso caldo de pescado carachama con yucas tiernas, sazonado con sacha culantro.", "S/. 26.00"),
            new Plato("Cecina con Patacones Crujientes", "Láminas de cecina frita servidas con patacones de plátano verde bien crocantes y sarza de ají charapita con cocona.", "S/. 29.00")
        )));
        
        platosPorCategoria.put("Bienriquito", new java.util.ArrayList<>(java.util.Arrays.asList(
            new Plato("El Mostrito del Pueblo", "Contundente plato que combina arroz chaufa de pollo de chifa y jugoso pollo a la brasa con papas fritas crujientes y todas las cremas.", "S/. 26.00"),
            new Plato("Chaufa Charapa con Plátano y Cecina", "Arroz chaufa salteado al wok con trozos de cecina ahumada, plátano maduro frito y sazonado con aceite de ajonjolí amazónico.", "S/. 28.00"),
            new Plato("Aeropuerto Taypá con Chicharrón", "Fideos y arroz chaufa salteados con frejolito chino, cebollita china, tortillas de huevo y coronado con crujiente chicharrón de chancho.", "S/. 24.00"),
            new Plato("Anticuchón Combinado Rachi y Pancita", "Brocheta gigante de anticucho acompañada de rachi sazonado y pancita de cordero a la parrilla, con papas y cremas.", "S/. 32.00"),
            new Plato("Salchipapa Monstruosa Especial", "Papas fritas abundantes con variedad de salchichas, chorizo parrillero, huevo frito, queso derretido y una lluvia de cremas caseras.", "S/. 22.00"),
            new Plato("Chancho a la Caja China Crujiente", "Porción generosa de panceta de cerdo crujiente hecha a la caja china con carapulcra al costado y yuca sancochada.", "S/. 38.00")
        )));
        
        platosPorCategoria.put("Precioso", new java.util.ArrayList<>(java.util.Arrays.asList(
            new Plato("Causa Gourmet de Cangrejo y Pulpa Real", "Suave masa de papa con infusión de ají amarillo, rellena de pulpa de cangrejo real, palta y un velo de mayonesa de olivo.", "S/. 36.00"),
            new Plato("Tiradito Fusión Rococó en Crema de Maracuyá", "Láminas delgadas de lenguado en emulsión cítrica de maracuyá y ají limo, con camote caramelizado y aire de limón.", "S/. 40.00"),
            new Plato("Lomo Fusión en Nido de Wantán", "Trozos de lomo fino salteados en reducción de salsa de ostión y pisco, servidos en una canasta crujiente de masa wantán sobre puré rústico.", "S/. 52.00"),
            new Plato("Suspiro de Limeña con Oporto Especiado", "Dulce de leche de consistencia cremosa perfumado con vainilla, coronado con merengue al oporto y una pizca de canela fina.", "S/. 16.00"),
            new Plato("Tres Leches de Pisco Acholado", "Bizcochuelo bañado en almíbar de tres leches y pisco acholado peruano, decorado con fresas y hojillas de menta.", "S/. 18.00"),
            new Plato("Domo de Lúcuma y Chocolate de Cacao Orgánico", "Mousse de lúcuma fresca bañado en espejo de chocolate negro al 70% de Quillabamba y decorado con láminas de oro comestible.", "S/. 24.00")
        )));
    }

    public static class Plato {
        private String nombre;
        private String descripcion;
        private String precio;

        public Plato(String nombre, String descripcion, String precio) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precio = precio;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getPrecio() {
            return precio;
        }
    }
}
