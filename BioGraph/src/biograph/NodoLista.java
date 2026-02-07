/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author 
 */
public class NodoLista<T> {
    public T dato;
    public NodoLista<T> siguiente;
    
    
    /**
     * Constructor para crear un nuevo nodo.
     * 
     * @param x dato
     * @param s Siguiente nodo.
     */
    public NodoLista(T x, NodoLista<T> s) {
        dato = x;
        siguiente = s;
    }
    
    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoLista<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoLista<T> siguiente) {
        this.siguiente = siguiente;
    }
}