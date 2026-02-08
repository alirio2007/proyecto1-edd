/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author Gianfranco, Adrian, Alirio
 */
public class Arista {
    /**
     * Atributos
     */
    private int destino;
    private int peso;
    
    /**
     * Constructor de la clase Arista
     * 
     * @param destino Índice de la proteína destino
     * @param peso Peso o costo de la interacción
     */
    public Arista(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }
    
    
    /**
     * getter y setter
     */
    public int getDestino() {
        return destino;
    }
    
    public int getPeso() {
        return peso;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    
   
    
    /**
     * Compara si esta arista es igual a otro objeto
     * Dos aristas se consideran iguales si tienen el mismo destin
     * 
     * @param obj Objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Arista arista = (Arista) obj;
        return destino == arista.destino;
    }
}
