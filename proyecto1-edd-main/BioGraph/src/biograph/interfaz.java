/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package biograph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;

/**
 * Interfaz gráfica principal de BioGraph
 * @author Gianfranco, Adrian, Alirio
 */
public class interfaz extends javax.swing.JFrame {
    
    // ====================================================
    // ATRIBUTOS DE LA CLASE
    // ====================================================
    private grafo miGrafo;
    private File archivoActual;
    private boolean cambiosGuardados;

    /**
     * Creates new form interfaz
     */
    public interfaz() {
        // Inicializar atributos
        miGrafo = new grafo(100, false);
        archivoActual = null;
        cambiosGuardados = true;
        
        // Inicializar componentes visuales
        initComponents();
        
        // Configuraciones adicionales
        this.setTitle("BioGraph - Análisis de Interacciones Proteicas");
        this.setLocationRelativeTo(null);
        pantalla.setEditable(false);
        
        // Configurar eventos
        configurarEventos();
        
        // Mostrar mensaje de bienvenida
        mostrarMensajeBienvenida();
    }
    
    // ====================================================
    // MÉTODOS DE CONFIGURACIÓN
    // ====================================================
    
    private void configurarEventos() {
        // Botones existentes
    carga.addActionListener(e -> cargarArchivo());
    actualizar.addActionListener(e -> guardarArchivo());
    deteccion.addActionListener(e -> detectarComplejos());
    ruta.addActionListener(e -> encontrarRuta());
    mostrar.addActionListener(e -> mostrarGrafo());
    if (agregar != null) {
        agregar.addActionListener(e -> agregarProteina());
    }
    if (eliminar != null) {
        eliminar.addActionListener(e -> eliminarProteina());
    } 
    }
    
    private void mostrarMensajeBienvenida() {
        pantalla.setText("BIENVENIDO A BIOGRAPH\n");
        pantalla.append("======================\n\n");
        pantalla.append("Sistema de Análisis de Interacciones Proteicas\n\n");
        pantalla.append("Funcionalidades:\n");
        pantalla.append("• Cargar archivo: Carga archivo CSV\n");
        pantalla.append("• Actualizar: Guarda cambios\n");
        pantalla.append("• Detectar complejos: BFS\n");
        pantalla.append("• Ruta más corta: Dijkstra\n");
        pantalla.append("• Mostrar grafo: Visualización con GraphStream\n");
        pantalla.append("• (Próximamente) Agregar/Eliminar aristas\n");
    }
    
    // ====================================================
    // MÉTODOS DE FUNCIONALIDAD PRINCIPAL (YA EXISTENTES)
    // ====================================================
    
    private void cargarArchivo() {
        if (!cambiosGuardados) {
            int opcion = JOptionPane.showConfirmDialog(this,
                "Hay cambios sin guardar. ¿Desea guardarlos?",
                "Cambios sin guardar",
                JOptionPane.YES_NO_CANCEL_OPTION);
            
            if (opcion == JOptionPane.CANCEL_OPTION) return;
            if (opcion == JOptionPane.YES_OPTION) {
                guardarArchivo();
            }
        }
        
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoActual = fileChooser.getSelectedFile();
            try {
                miGrafo.cargarArchivo(archivoActual);
                cambiosGuardados = true;
                pantalla.setText("ARCHIVO CARGADO: " + archivoActual.getName() + "\n\n");
                pantalla.append(miGrafo.toString());
                JOptionPane.showMessageDialog(this, "Archivo cargado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void guardarArchivo() {
        if (!miGrafo.tieneDatos()) {
            JOptionPane.showMessageDialog(this, "No hay datos para guardar");
            return;
        }
        
        if (archivoActual == null) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                archivoActual = fileChooser.getSelectedFile();
                if (!archivoActual.getName().toLowerCase().endsWith(".csv")) {
                    archivoActual = new File(archivoActual.getAbsolutePath() + ".csv");
                }
            } else {
                return;
            }
        }
        
        try {
            miGrafo.guardarArchivo(archivoActual);
            cambiosGuardados = true;
            JOptionPane.showMessageDialog(this, "Archivo guardado correctamente");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void detectarComplejos() {
        if (!miGrafo.tieneDatos()) {
            JOptionPane.showMessageDialog(this, "No hay datos cargados");
            return;
        }
        
        Lista<Lista<String>> complejos = miGrafo.encontrarComplejosProteicos();
        
        pantalla.setText("COMPLEJOS PROTEICOS DETECTADOS\n");
        pantalla.append("===============================\n\n");
        
        if (complejos.obtenerInicio() == null) {
            pantalla.append("No se encontraron complejos");
        } else {
            NodoLista<Lista<String>> nodo = complejos.obtenerInicio();
            int num = 1;
            while (nodo != null) {
                pantalla.append("Complejo " + num + ":\n");
                Lista<String> complejo = nodo.getDato();
                NodoLista<String> prot = complejo.obtenerInicio();
                while (prot != null) {
                    pantalla.append("  • " + prot.getDato() + "\n");
                    prot = prot.getSiguiente();
                }
                pantalla.append("\n");
                nodo = nodo.getSiguiente();
                num++;
            }
        }
    }
    
    private void encontrarRuta() {
        if (!miGrafo.tieneDatos()) {
            JOptionPane.showMessageDialog(this, "No hay datos cargados");
            return;
        }
        
        JPanel panel = new JPanel(new java.awt.GridLayout(2, 2, 5, 5));
        JTextField origenField = new JTextField();
        JTextField destinoField = new JTextField();
        
        panel.add(new JLabel("Proteína ORIGEN:"));
        panel.add(origenField);
        panel.add(new JLabel("Proteína DESTINO:"));
        panel.add(destinoField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Ruta Metabólica más Corta", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String origen = origenField.getText().trim();
            String destino = destinoField.getText().trim();
            
            if (origen.isEmpty() || destino.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Los nombres no pueden estar vacíos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Lista<String> ruta = miGrafo.encontrarRutaMasCorta(origen, destino);
            
            pantalla.setText("RUTA MÁS CORTA\n");
            pantalla.append("==============\n\n");
            pantalla.append("De: " + origen + "\n");
            pantalla.append("A: " + destino + "\n\n");
            
            if (ruta.obtenerInicio() == null) {
                pantalla.append("No hay ruta disponible");
            } else {
                pantalla.append("Ruta:\n");
                NodoLista<String> nodo = ruta.obtenerInicio();
                while (nodo != null) {
                    pantalla.append("  → " + nodo.getDato() + "\n");
                    nodo = nodo.getSiguiente();
                }
            }
        }
    }
    
    // ====================================================
    // MÉTODOS NUEVOS PARA AGREGAR Y ELIMINAR ARISTAS
    // ====================================================
    
    /**
     * Agrega una nueva arista (interacción) entre dos proteínas
     */
    private void agregarProteina() {
        // PASO 1: Solicitar nombre de la nueva proteína
    String nombre = JOptionPane.showInputDialog(this,
        "Ingrese el nombre de la nueva proteína:",
        "Agregar Proteína - Paso 1",
        JOptionPane.QUESTION_MESSAGE);
    
    if (nombre == null || nombre.trim().isEmpty()) {
        return; // Usuario canceló
    }
    
    nombre = nombre.trim();
    
    try {
        // Verificar si ya existe
        if (miGrafo.getIndiceProteina(nombre) != -1) {
            JOptionPane.showMessageDialog(this,
                "Ya existe una proteína con el nombre '" + nombre + "'",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // PASO 2: Agregar la proteína al grafo
        int indiceNueva = miGrafo.agregarProteina(nombre);
        cambiosGuardados = false;
        
        // PASO 3: Obtener lista de proteínas existentes (excluyendo la nueva)
        String[] proteinasExistentes = miGrafo.obtenerProteinasActivas();
        
        // Filtrar para quitar la proteína recién agregada
        java.util.ArrayList<String> listaTemp = new java.util.ArrayList<>();
        for (String p : proteinasExistentes) {
            if (!p.equals(nombre)) {
                listaTemp.add(p);
            }
        }
        
        if (listaTemp.isEmpty()) {
            // No hay otras proteínas para conectar
            JOptionPane.showMessageDialog(this,
                "Proteína '" + nombre + "' agregada correctamente.\n" +
                "No hay otras proteínas para conectar.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            pantalla.setText("PROTEÍNA AGREGADA\n");
            pantalla.append("=================\n\n");
            pantalla.append("Nombre: " + nombre + "\n");
            pantalla.append("Índice: " + indiceNueva + "\n\n");
            pantalla.append("(No hay otras proteínas para conectar)\n\n");
            pantalla.append("ESTADO ACTUAL DEL GRAFO:\n\n");
            pantalla.append(miGrafo.toString());
            return;
        }
        
        String[] otrasProteinas = listaTemp.toArray(new String[0]);
        
        // PASO 4: Preguntar si quiere agregar conexiones
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desea conectar '" + nombre + "' con otras proteínas?",
            "Agregar Proteína - Paso 2",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            // PASO 5: Panel para agregar múltiples conexiones
            boolean seguirAgregando = true;
            int conexionesAgregadas = 0;
            
            while (seguirAgregando) {
                // Crear panel para una conexión
                JPanel panel = new JPanel(new java.awt.GridLayout(2, 2, 10, 10));
                
                JComboBox<String> comboDestino = new JComboBox<>(otrasProteinas);
                JTextField txtPeso = new JTextField("10");
                
                panel.add(new JLabel("Conectar con:"));
                panel.add(comboDestino);
                panel.add(new JLabel("Peso de la interacción:"));
                panel.add(txtPeso);
                
                int result = JOptionPane.showConfirmDialog(this, panel,
                    "Agregar Conexión para " + nombre + " (Nueva conexión)",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
                
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String destino = (String) comboDestino.getSelectedItem();
                        int peso = Integer.parseInt(txtPeso.getText().trim());
                        
                        if (peso <= 0) {
                            JOptionPane.showMessageDialog(this,
                                "El peso debe ser un número positivo",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        
                        // Verificar si ya existe la conexión
                        if (miGrafo.existeInteraccion(nombre, destino)) {
                            JOptionPane.showMessageDialog(this,
                                "Ya existe una conexión entre " + nombre + " y " + destino,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        
                        // Agregar la interacción
                        miGrafo.agregarInteraccion(nombre, destino, peso);
                        conexionesAgregadas++;
                        cambiosGuardados = false;
                        
                        JOptionPane.showMessageDialog(this,
                            "Conexión agregada: " + nombre + " ↔ " + destino + " (peso: " + peso + ")",
                            "Conexión agregada",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this,
                            "El peso debe ser un número entero válido",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    seguirAgregando = false;
                }
                
                // Preguntar si quiere agregar otra conexión
                if (seguirAgregando) {
                    int continuar = JOptionPane.showConfirmDialog(this,
                        "¿Desea agregar otra conexión para " + nombre + "?",
                        "Agregar otra conexión",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (continuar != JOptionPane.YES_OPTION) {
                        seguirAgregando = false;
                    }
                }
            }
            
            // Mensaje final
            JOptionPane.showMessageDialog(this,
                "Proteína '" + nombre + "' agregada correctamente.\n" +
                "Se agregaron " + conexionesAgregadas + " conexiones.",
                "Proceso completado",
                JOptionPane.INFORMATION_MESSAGE);
            
        } else {
            // No quiso agregar conexiones
            JOptionPane.showMessageDialog(this,
                "Proteína '" + nombre + "' agregada correctamente.\n" +
                "No se agregaron conexiones.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Actualizar pantalla
        pantalla.setText("PROTEÍNA AGREGADA: " + nombre + "\n");
        pantalla.append("========================\n\n");
        pantalla.append("ESTADO ACTUAL DEL GRAFO:\n\n");
        pantalla.append(miGrafo.toString());
        
    } catch (RuntimeException e) {
        JOptionPane.showMessageDialog(this,
            "Error al agregar proteína: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    }
    
    /**
     * Elimina una arista (interacción) existente entre dos proteínas
     */
    private void eliminarProteina() {
        if (!miGrafo.tieneDatos()) {
        JOptionPane.showMessageDialog(this,
            "No hay proteínas para eliminar",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Obtener lista de proteínas activas para mostrarlas
    String[] proteinas = miGrafo.obtenerProteinasActivas();
    
    // Crear un combo box para seleccionar la proteína
    JComboBox<String> comboProteinas = new JComboBox<>(proteinas);
    
    JPanel panel = new JPanel(new java.awt.GridLayout(1, 2, 10, 10));
    panel.add(new JLabel("Seleccione proteína a eliminar:"));
    panel.add(comboProteinas);
    
    int result = JOptionPane.showConfirmDialog(this, panel,
        "Eliminar Proteína",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE);
    
    if (result == JOptionPane.OK_OPTION) {
        String nombre = (String) comboProteinas.getSelectedItem();
        
        // Confirmar eliminación
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar la proteína '" + nombre + "'?\n" +
            "Esta acción eliminará todas sus interacciones.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            miGrafo.eliminarProteina(nombre);
            cambiosGuardados = false;
            
            JOptionPane.showMessageDialog(this,
                "Proteína '" + nombre + "' eliminada correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Actualizar pantalla
            pantalla.setText("PROTEÍNA ELIMINADA\n");
            pantalla.append("==================\n\n");
            pantalla.append("Se eliminó la proteína: " + nombre + "\n\n");
            pantalla.append("ESTADO ACTUAL DEL GRAFO:\n\n");
            pantalla.append(miGrafo.toString());
        }
    }
    }
    
    /**
     * Método para mostrar el grafo visualmente usando GraphStream
     */
    private void mostrarGrafo() {
        if (!miGrafo.tieneDatos()) {
            JOptionPane.showMessageDialog(this,
                "No hay datos para mostrar",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Configurar el motor de UI
            System.setProperty("org.graphstream.ui", "swing");
            
            // Crear el grafo de GraphStream
            org.graphstream.graph.Graph graphStream = 
                new org.graphstream.graph.implementations.SingleGraph("BioGraph");
            
            // Mejorar la calidad visual
            graphStream.setAttribute("ui.quality");
            graphStream.setAttribute("ui.antialias");
            
            // 1. AGREGAR NODOS (proteínas activas)
            String[] proteinas = miGrafo.obtenerProteinasActivas();
            
            for (String proteina : proteinas) {
                org.graphstream.graph.Node nodo = graphStream.addNode(proteina);
                
                // Personalizar apariencia del nodo
                nodo.setAttribute("ui.label", proteina);
                nodo.setAttribute("ui.style", 
                    "fill-color: rgb(100,150,200);" +
                    "size: 30px;" +
                    "text-size: 14px;" +
                    "text-style: bold;" +
                    "text-color: white;" +
                    "stroke-mode: plain;" +
                    "stroke-color: black;" +
                    "stroke-width: 2px;");
            }
            
            // 2. AGREGAR ARISTAS (interacciones)
            for (String proteina : proteinas) {
                int idxOrigen = miGrafo.getIndiceProteina(proteina);
                
                if (idxOrigen == -1) continue;
                
                Lista<Arista> listaAdy = miGrafo.getListaAdyacencia(idxOrigen);
                if (listaAdy == null) continue;
                
                NodoLista<Arista> nodo = listaAdy.obtenerInicio();
                
                while (nodo != null) {
                    Arista arista = nodo.getDato();
                    int idxDestino = arista.getDestino();
                    String proteinaDestino = miGrafo.getNombreProteina(idxDestino);
                    
                    if (proteinaDestino != null) {
                        String idArista = proteina + "-" + proteinaDestino;
                        String idAristaInversa = proteinaDestino + "-" + proteina;
                        
                        if (graphStream.getEdge(idArista) == null && 
                            graphStream.getEdge(idAristaInversa) == null) {
                            
                            org.graphstream.graph.Edge edge = 
                                graphStream.addEdge(idArista, proteina, proteinaDestino);
                            
                            int peso = arista.getPeso();
                            edge.setAttribute("ui.label", String.valueOf(peso));
                            
                            // Color según peso
                            String color;
                            if (peso < 15) color = "green";
                            else if (peso < 30) color = "orange";
                            else color = "red";
                            
                            edge.setAttribute("ui.style", 
                                "fill-color: " + color + ";" +
                                "size: 2px;" +
                                "text-size: 11px;" +
                                "text-color: black;" +
                                "text-background-mode: rounded-box;" +
                                "text-background-color: white;" +
                                "text-padding: 3px;" +
                                "arrow-shape: none;");
                        }
                    }
                    nodo = nodo.getSiguiente();
                }
            }
            
            // 3. DESTACAR HUBS (proteínas con más conexiones)
            Lista<String> hubs = miGrafo.identificarHubs(3);
            NodoLista<String> hubNodo = hubs.obtenerInicio();
            
            while (hubNodo != null) {
                String hubName = hubNodo.getDato();
                org.graphstream.graph.Node hub = graphStream.getNode(hubName);
                if (hub != null) {
                    hub.setAttribute("ui.style", 
                        "fill-color: rgb(200,50,50);" +
                        "size: 40px;" +
                        "text-size: 16px;" +
                        "text-style: bold;" +
                        "text-color: white;" +
                        "stroke-mode: plain;" +
                        "stroke-color: gold;" +
                        "stroke-width: 3px;");
                }
                hubNodo = hubNodo.getSiguiente();
            }
            
            // 4. MOSTRAR EL GRAFO
            javax.swing.SwingUtilities.invokeLater(() -> {
                graphStream.display();
            });
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al crear la visualización: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
            // Mostrar el grafo en texto como fallback
            pantalla.setText("ERROR DE VISUALIZACIÓN GRÁFICA\n");
            pantalla.append("===============================\n\n");
            pantalla.append("Error: " + e.getMessage() + "\n\n");
            pantalla.append("Mostrando representación en texto:\n\n");
            pantalla.append(miGrafo.toString());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
            

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        deteccion = new javax.swing.JButton();
        carga = new javax.swing.JButton();
        ruta = new javax.swing.JButton();
        mostrar = new javax.swing.JButton();
        actualizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pantalla = new javax.swing.JTextArea();
        eliminar = new javax.swing.JButton();
        agregar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("BioGraph");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 20));

        jLabel2.setText("Adrian, Alirio, Gianfranco");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        deteccion.setText("Detección de Complejos Proteicos");
        jPanel1.add(deteccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        carga.setText("Cargar archivo");
        jPanel1.add(carga, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        ruta.setText("Ruta Metabolica mas Corta");
        ruta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rutaActionPerformed(evt);
            }
        });
        jPanel1.add(ruta, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        mostrar.setText("Mostrar grafo");
        jPanel1.add(mostrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        actualizar.setText("Actualizar repositorio");
        jPanel1.add(actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, -1, -1));

        pantalla.setColumns(20);
        pantalla.setRows(5);
        jScrollPane1.setViewportView(pantalla);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 400, 390));

        eliminar.setText("Eliminar Proteina");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });
        jPanel1.add(eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        agregar.setText("Agregar Proteina");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });
        jPanel1.add(agregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 540));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rutaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rutaActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_agregarActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eliminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new interfaz().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JButton agregar;
    private javax.swing.JButton carga;
    private javax.swing.JButton deteccion;
    private javax.swing.JButton eliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton mostrar;
    private javax.swing.JTextArea pantalla;
    private javax.swing.JButton ruta;
    // End of variables declaration//GEN-END:variables
}
