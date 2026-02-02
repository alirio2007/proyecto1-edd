/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author 
 */
public class NodoLista {
    public int dato;
    public NodoLista siguiente;
    
    
    /**
     * Constructor para crear un nuevo nodo.
     * 
     * @param x dato
     * @param s Siguiente nodo.
     */
    public NodoLista(int x, NodoLista s) {
        dato = x;
        siguiente = s;
    }
    
    public int getDato() {
        return dato;
    }

    public void setDato(int dato) {
        this.dato = dato;
    }

    public NodoLista getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoLista siguiente) {
        this.siguiente = siguiente;
    }
}
