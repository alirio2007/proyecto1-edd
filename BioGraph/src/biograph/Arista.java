/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author 
 */
public class Arista {
    private int destino;
    private int peso;
    
    public Arista(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }
    
    public int getDestino() {
        return destino;
    }
    
    public int getPeso() {
        return peso;
    }
    
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
