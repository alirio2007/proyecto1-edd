/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package biograph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

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

    private void mostrarGrafo() {
        // TODO: Programar esta función 
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
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

        ruta.setText("Ruta Metabólica más Corta:");
        jPanel1.add(ruta, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        mostrar.setText("Mostrar grafo");
        jPanel1.add(mostrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        actualizar.setText("Actualizar repositorio");
        jPanel1.add(actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, -1, -1));

        pantalla.setColumns(20);
        pantalla.setRows(5);
        jScrollPane1.setViewportView(pantalla);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 400, 390));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 540));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JButton carga;
    private javax.swing.JButton deteccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton mostrar;
    private javax.swing.JTextArea pantalla;
    private javax.swing.JButton ruta;
    // End of variables declaration//GEN-END:variables
}
