/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author Gianfranco, Adrian, Alirio
 */
public class Pila<T> {
    /**
     * Atributos
     */
    private NodoLista<T> cima;
    private int tamaño;
    
    /**
     * Constructor de pila vacia
     */
    public Pila() {
        cima = null;
        tamaño = 0;
    }
    
     /**
     * Verifica si la pila está vacía.
     * 
     * @return true si la pila está vacía, false en caso contrario
     */
    public boolean estaVacia() {
        return cima == null;
    }
    
    /**
     * Inserta un elemento en la cima de la pila.
     * 
     * @param valor Elemento a insertar
     */
    public void push(T valor) {
        NodoLista<T> nuevo = new NodoLista<>(valor, cima);
        cima = nuevo;
        tamaño++;
    }
    
    /**
     * Elimina y retorna el elemento en la cima de la pila.
     * 
     * @return Elemento en la cima de la pila
     * @throws IllegalStateException si la pila está vacía
     */
    public T pop() {
        if (estaVacia()) {
            throw new IllegalStateException("Pila vacía");
        }
        T valor = cima.getDato();
        cima = cima.getSiguiente();
        tamaño--;
        return valor;
    }
    
    /**
     * Retorna el elemento en la cima de la pila sin eliminarlo.
     * 
     * @return Elemento en la cima de la pila
     * @throws IllegalStateException si la pila está vacía
     */
    public T peek() {
        if (estaVacia()) {
            throw new IllegalStateException("Pila vacía");
        }
        return cima.getDato();
    }
    
    /**
     * Retorna el número de elementos en la pila.
     * 
     * @return Tamaño de la pila
     */
    public int tamaño() {
        return tamaño;
    }
}
