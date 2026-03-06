/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package biograph;

/**
 * Clase principal que inicia la aplicación BioGraph
 * @author Gianfranco, Adrian, Alirio
 */
public class Biograph {

    /**
     * Método principal que inicia la interfaz gráfica
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Iniciar la interfaz gráfica en el hilo de eventos de Swing
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new interfaz().setVisible(true);
            }
        });
    }
    
}